// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;

/** Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 82 */
/* package */ enum SpdMetricSquared implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Tensor pn = MatrixSqrt.ofSymmetric(p).inverse();
    Tensor pq = Symmetrize.of(pn.dot(q).dot(pn));
    return SpdMatrixExponential.nSquared(pq);
  }
}
