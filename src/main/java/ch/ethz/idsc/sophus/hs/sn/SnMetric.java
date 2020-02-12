// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.VectorAngle;

/** The distance between two point on the d-dimensional sphere
 * embedded in R^(d+1) is the vector angle between the points. */
public enum SnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return VectorAngle.of(p, q).get();
  }
}
