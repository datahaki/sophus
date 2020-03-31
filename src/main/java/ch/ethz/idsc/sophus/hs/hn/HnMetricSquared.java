// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** @see HnMetric */
public enum HnMetricSquared implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    Scalar scalar = HnMetric.INSTANCE.distance(x, y);
    return scalar.multiply(scalar);
  }
}
