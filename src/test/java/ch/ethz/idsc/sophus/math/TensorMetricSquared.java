// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/* package */ class TensorMetricSquared implements TensorMetric, Serializable {
  /** @param tensorMetric
   * @return */
  public static TensorMetric of(TensorMetric tensorMetric) {
    return new TensorMetricSquared(Objects.requireNonNull(tensorMetric));
  }

  /***************************************************/
  private final TensorMetric tensorMetric;

  private TensorMetricSquared(TensorMetric tensorMetric) {
    this.tensorMetric = tensorMetric;
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar distance = tensorMetric.distance(p, q);
    return distance.multiply(distance);
  }
}
