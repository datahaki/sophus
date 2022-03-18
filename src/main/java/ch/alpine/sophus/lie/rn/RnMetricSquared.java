// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2NormSquared;

/** Euclidean vector metric */
public enum RnMetricSquared implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Vector2NormSquared.between(p, q);
  }
}
