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
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.io.StringScalar;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.ad.NilpotentAlgebraQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.N;

/** https://en.wikipedia.org/wiki/Symmetric_space */
public class HsAlgebra implements Serializable {
  private static final int MAX_ITERATIONS = 100;
  // ---
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
   * @return projection( bch(g, [m 0]) ) */
  public Tensor action(Tensor g, Tensor m) {
    return projection(bch.apply(g, lift(m)));
  }

  /** @param m
   * @return [m 0] */
  public Tensor lift(Tensor m) {
    return Join.of(VectorQ.requireLength(m, dim_m), pad);
  }

  /** @param g vector with length dim_g
   * @return m for which there is a h with bch(g, h) == [m 0] */
  public Tensor projection(Tensor g) {
    return new Decomp(g).m;
  }

  /** achieves decomposition of g into m and h simultaneously with
   * bch(g, h) == lift(m), or equivalently
   * g == bch(lift(m), -h) */
  public class Decomp implements Serializable {
    /** vector of length dim_m */
    public final Tensor m;
    /** vector of length dim_g with first dim_m entries zero */
    public final Tensor h;

    public Decomp(Tensor g) {
      Tensor mi = VectorQ.requireLength(g, dim_g); // residual
      Tensor hi = Array.zeros(dim_g); // initial h estimate
      for (int count = 0; count < MAX_ITERATIONS; ++count) {
        if (Tolerance.CHOP.allZero(mi.extract(dim_m, dim_g))) {
          m = mi.extract(0, dim_m);
          h = hi;
          return;
        }
        Tensor hd = approxHInv(mi); // delta
        mi = bch.apply(mi, hd); // towards bch(lift(m), -h) == g
        hi = bch.apply(hi, hd); // towards bch(g, h) == lift(m)
      }
      throw TensorRuntimeException.of(g);
    }

    /** @param g
     * @return [0 -g|h] */
    private Tensor approxHInv(Tensor g) {
      return Join.of(Array.zeros(dim_m), g.extract(dim_m, dim_g).negate());
    }
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

  public LieAlgebra lieAlgebra() {
    return new LieAlgebraImpl(ad);
  }
}