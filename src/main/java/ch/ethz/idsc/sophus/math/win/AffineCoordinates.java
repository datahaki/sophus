// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Mean;

/** Affine coordinates created by n points in d-dimensional vector space
 * S. Waldron. Affine generalized barycentric coordinates. Jaen Journal on Approximation, 3(2):209-226, 2011 */
public class AffineCoordinates implements TensorUnaryOperator {
  /** @param points matrix with dimensions n x d
   * @return
   * @throws Exception if points is empty */
  public static TensorUnaryOperator of(Tensor points) {
    return new AffineCoordinates(points);
  }

  // ---
  private final Tensor mean;
  private final Tensor pinv;
  private final Scalar _1_n;

  private AffineCoordinates(Tensor points) {
    mean = Mean.of(points);
    pinv = PseudoInverse.of(Tensor.of(points.stream().map(mean.negate()::add)));
    _1_n = RationalScalar.of(1, points.length());
  }

  @Override
  public Tensor apply(Tensor x) {
    return x.subtract(mean).dot(pinv).map(_1_n::add);
  }
}
