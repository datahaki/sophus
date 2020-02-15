// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** The distance between two point on the d-dimensional sphere
 * embedded in R^(d+1) is the vector angle between the points. */
public enum SnMetricSquared implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar scalar = SnMetric.INSTANCE.distance(p, q);
    return scalar.multiply(scalar);
  }
}
