// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Mean;

/** Affine coordinates
 * S. Waldron. Affine generalized barycentric coordinates. Jaen Journal on Approximation, 3(2):209-226, 2011 */
public class AffineCoordinates implements TensorUnaryOperator {
  public static TensorUnaryOperator of(Tensor points) {
    return new AffineCoordinates(points);
  }

  // ---
  private final Tensor mean;
  private final Tensor pinv;
  private final Scalar oon;

  private AffineCoordinates(Tensor points) {
    mean = Mean.of(points);
    Tensor v = Tensor.of(points.stream().map(mean.negate()::add));
    Tensor vt = Transpose.of(v);
    pinv = LinearSolve.of(vt.dot(v), vt); // see LeastSquares with b == Id
    // pinv = PseudoInverse.of(v);
    oon = RationalScalar.of(1, points.length());
  }

  @Override
  public Tensor apply(Tensor x) {
    return x.subtract(mean).dot(pinv).map(oon::add);
  }
}
