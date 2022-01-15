// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.io.StringScalar;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.ad.NilpotentAlgebraQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

/** https://en.wikipedia.org/wiki/Symmetric_space */
public class HsAlgebra implements Serializable {
  private final Tensor ad;
  private final int dim_m;
  private final int dim_g;
  private final int dim_h;
  private final BinaryOperator<Tensor> bch;
  private final Tensor pad;

  public HsAlgebra(Tensor ad, int dim_m, int degree) {
    this.ad = ad;
    // ---
    this.dim_m = dim_m;
    dim_g = ad.length();
    dim_h = dim_g - dim_m;
    pad = Array.zeros(dim_h);
    boolean isNilpotent = NilpotentAlgebraQ.of(ad);
    bch = BakerCampbellHausdorff.of(isNilpotent ? ad : N.DOUBLE.of(ad), degree);
    // ---
    consistencyCheck();
  }

  /** @param g vector with length dim_g
   * @param m vector with length dim_m
   * @return vector with length dim_m */
  public Tensor action(Tensor g, Tensor m) {
    Tensor z = bch.apply(g, Join.of(m, pad));
    Tensor h = projection(z);
    Tensor a = bch.apply(z, h);
    // TODO remove check once tested
    Chop._05.requireAllZero(a.extract(dim_m, dim_g));
    return a.extract(0, dim_m);
  }

  /** @param g
   * @return h so that bch(g, h) == [m 0] */
  public Tensor projection(Tensor g) {
    Tensor r = g.copy();
    Tensor h = g.map(Scalar::zero);
    while (!Tolerance.CHOP.allZero(r.extract(dim_m, dim_g))) {
      Tensor s = r.copy();
      for (int k = 0; k < dim_m; ++k)
        s.set(Scalar::zero, k);
      for (int k = dim_m; k < dim_g; ++k)
        s.set(Scalar::negate, k);
      r = bch.apply(r, s);
      h = bch.apply(h, s);
    }
    { // check
      // TODO remove check once tested
      Chop._05.requireAllZero(bch.apply(g, h).extract(dim_m, dim_g));
    }
    return h;
  }

  public int dimG() {
    return dim_g;
  }

  public int dimM() {
    return dim_m;
  }

  public int dimH() {
    return dim_h;
  }

  /** function asserts [h, h] subset h which is a requirement for any homogeneous space */
  private void consistencyCheck() {
    if (!Transpose.of(ad, 0, 2, 1).add(ad) //
        .flatten(-1) //
        .map(Scalar.class::cast) //
        .allMatch(Scalars::isZero))
      throw TensorRuntimeException.of(ad);
    if (!ad.block(Arrays.asList(0, dim_m, dim_m), Arrays.asList(dim_m, dim_h, dim_h)) //
        .flatten(-1) //
        .map(Scalar.class::cast) //
        .allMatch(Scalars::isZero))
      throw TensorRuntimeException.of(ad);
  }

  /** @return whether [h, m] subset m, i.e. h cap [h, m] = {0} */
  public boolean isReductive() {
    return ad.block(Arrays.asList(dim_m, dim_m, 0), Arrays.asList(dim_h, dim_h, dim_m)) //
        .flatten(-1) //
        .map(Scalar.class::cast) //
        .allMatch(Scalars::isZero);
  }

  /** @return whether [m, m] subset h, i.e. m cap [m, m] = {0} */
  public boolean isSymmetric() {
    return ad.block(Arrays.asList(0, 0, 0), Arrays.asList(dim_m, dim_m, dim_m)) //
        .flatten(-1) //
        .map(Scalar.class::cast) //
        .allMatch(Scalars::isZero);
  }

  public void printTable() {
    Tensor array = Array.fill(() -> StringScalar.EMPTY, dim_g, dim_g);
    for (int i = 0; i < dim_g; ++i) {
      for (int j = 0; j < dim_g; ++j) {
        Tensor coeffs = ad.dot(UnitVector.of(dim_g, i)).dot(UnitVector.of(dim_g, j));
        List<String> list = new LinkedList<>();
        for (int k = 0; k < dim_m; ++k)
          if (Scalars.nonZero(coeffs.Get(k)))
            list.add(coeffs.Get(k) + "*m" + k);
        for (int k = 0; k < dim_h; ++k)
          if (Scalars.nonZero(coeffs.Get(dim_m + k)))
            list.add(coeffs.Get(dim_m + k) + "*h" + k);
        array.set(StringScalar.of(list.stream().collect(Collectors.joining("+"))), i, j);
      }
    }
    System.out.println(Pretty.of(array));
  }
}
