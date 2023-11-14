// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.dv.AffineCoordinate;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.red.Mean;

/** Affine coordinates created by n points in d-dimensional vector space
 * 
 * Affine coordinates are generalized barycentric coordinates and satisfy
 * partition of unity property
 * linear reproduction property
 * C^infinity
 * 
 * but do not satisfy Lagrange property
 * 
 * Reference:
 * "Affine generalized barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
 * 
 * Quote: "Among all generalized barycentric coordinates with respect to a
 * scattered set of points, Waldron suggests to consider those with minimal
 * L2-norm, which are uniquely defined as the affine functions."
 * in Kai Hormann, N. Sukumar, 2017
 * 
 * <p>The concept of a one-time computation of a pseudo-inverse at a central
 * location does not generalize to non-linear spaces.
 * 
 * @see AffineCoordinate */
public enum RnAffineCoordinate implements BarycentricCoordinate {
  INSTANCE;

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return of(sequence).apply(point);
  }

  /** @param sequence matrix with dimensions n x d
   * @return
   * @throws Exception if given sequence is empty */
  public static TensorUnaryOperator of(Tensor sequence) {
    return new Inner(sequence);
  }

  private static class Inner implements TensorUnaryOperator {
    private final Scalar _1_n;
    /** negative mean */
    private final Tensor negm;
    private final Tensor pinv;

    private Inner(Tensor sequence) {
      _1_n = RationalScalar.of(1, sequence.length());
      negm = Mean.of(sequence).negate();
      pinv = PseudoInverse.of(Tensor.of(sequence.stream().map(negm::add)));
    }

    @Override
    public Tensor apply(Tensor x) {
      return x.add(negm).dot(pinv).map(_1_n::add);
    }
  }
}
