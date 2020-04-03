// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Mean;

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
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
 * 
 * Quote: "Among all generalized barycentric coordinates with respect to a
 * scattered set of points, Waldron suggests to consider those with minimal
 * L2-norm, which are uniquely defined as the affine functions."
 * in Kai Hormann, N. Sukumar, 2017 */
public enum AffineCoordinate implements BarycentricCoordinate {
  INSTANCE;

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return of(sequence).apply(point);
  }

  /** @param sequence matrix with dimensions n x d
   * @return
   * @throws Exception if given sequence is empty */
  public static TensorUnaryOperator of(Tensor sequence) {
    return new Operator(sequence);
  }

  private static class Operator implements TensorUnaryOperator {
    private final Scalar _1_n;
    private final Tensor mean;
    private final Tensor pinv;

    private Operator(Tensor sequence) {
      _1_n = RationalScalar.of(1, sequence.length());
      mean = Mean.of(sequence);
      pinv = PseudoInverse.of(Tensor.of(sequence.stream().map(mean.negate()::add)));
    }

    @Override
    public Tensor apply(Tensor x) {
      return x.subtract(mean).dot(pinv).map(_1_n::add);
    }
  }
}
