// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.sophus.lie.NilpotentAlgebraQ;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.io.StringScalar;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

/** https://en.wikipedia.org/wiki/Symmetric_space */
public class HsAlgebra implements HsLocal, Serializable {
  private static final int MAX_ITERATIONS = 100;
  // ---
  private final Tensor ad;
  private final int dim_m;
  private final int dim_g;
  private final int dim_h;
  private final TensorBinaryOperator bch;
  private final Tensor pad;

  public HsAlgebra(Tensor ad, int dim_m, int degree) {
    this.ad = ad;
    // ---
    this.dim_m = dim_m;
    dim_g = ad.length();
    dim_h = dim_g - dim_m;
    pad = Array.zeros(dim_h);
    boolean isNilpotent = NilpotentAlgebraQ.of(ad);
    bch = BakerCampbellHausdorff.of(isNilpotent ? ad : ad.maps(N.DOUBLE), degree);
    // ---
    consistencyCheck();
  }

  public Tensor ad() {
    return ad;
  }

  /** @param g vector with length dim_g
   * @param m vector with length dim_m
   * @return projection( bch(g, [m 0]) ) */
  @Override // from HomogeneousSpace
  public Tensor action(Tensor g, Tensor m) {
    return projection(bch.apply(g, lift(m)));
  }

  /** @param g vector with length dim_g
   * @return */
  public TensorUnaryOperator action(Tensor g) {
    return m -> action(g, m);
  }

  /** @param g vector with length dim_g
   * @return m for which there is a h with bch(g, h) == [m 0] */
  @Override // from HomogeneousSpace
  public Tensor projection(Tensor g) {
    return new Decomp(HsPair.seed(g)).m;
  }

  /** @param m
   * @return [m 0] */
  @Override // from HomogeneousSpace
  public Tensor lift(Tensor m) {
    return Join.of(VectorQ.requireLength(m, dim_m), pad);
  }

  /** achieves decomposition of g into m and h simultaneously with
   * bch(g, h) == lift(m), or equivalently
   * g == bch(lift(m), -h) */
  public class Decomp implements Serializable {
    /** vector of length dim_m */
    public final Tensor m;
    /** vector of length dim_g with first dim_m entries zero */
    public final Tensor h;

    public Decomp(HsPair hsPair) {
      for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
        if (Chop._14.allZero(hsPair.g().extract(dim_m, dim_g))) {
          m = hsPair.g().extract(0, dim_m);
          h = hsPair.h();
          return;
        }
        hsPair = hsPair.move(bch, approxHInv(hsPair.g()));
      }
      throw new IllegalStateException();
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
    if (!Flatten.scalars(Transpose.of(ad, 0, 2, 1).add(ad)) //
        .allMatch(Scalars::isZero))
      throw new Throw(ad);
    if (!Flatten.scalars(ad.block(List.of(0, dim_m, dim_m), List.of(dim_m, dim_h, dim_h))) //
        .allMatch(Scalars::isZero))
      throw new Throw(ad);
  }

  /** @return whether [h, m] subset m, i.e. h cap [h, m] = {0} */
  public boolean isReductive() {
    return Flatten.scalars(ad.block(List.of(dim_m, dim_m, 0), List.of(dim_h, dim_h, dim_m))) //
        .allMatch(Scalars::isZero);
  }

  /** @return whether [m, m] subset h, i.e. m cap [m, m] = {0} */
  public boolean isSymmetric() {
    return Flatten.scalars(ad.block(List.of(0, 0, 0), List.of(dim_m, dim_m, dim_m))) //
        .allMatch(Scalars::isZero);
  }

  /** @return whether [m, g] subset m, in which case finding the element h that
   * projects g to m is trivial */
  public boolean isHTrivial() {
    return Flatten.scalars(ad.block(List.of(dim_m, 0, 0), List.of(dim_h, dim_g, dim_m))) //
        .allMatch(Scalars::isZero);
  }

  public void printTable() {
    Tensor array = Array.same(StringScalar.empty(), dim_g, dim_g);
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
        array.set(StringScalar.of(String.join("+", list)), i, j);
      }
    }
    IO.println(Pretty.of(array));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("HsAlgebra", dim_m, dim_h);
  }
}
