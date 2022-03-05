// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 82 */
public enum SpdMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return new SpdExponential(p).distance(q);
  }
}
