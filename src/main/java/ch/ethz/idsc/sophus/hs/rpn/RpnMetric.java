// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.hs.sn.SnMetric;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Min;

/**  */
public enum RpnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    // TODO inefficient
    Scalar d_xyp = SnMetric.INSTANCE.distance(x, y);
    Scalar d_xyn = SnMetric.INSTANCE.distance(x, y.negate());
    return Min.of(d_xyp, d_xyn);
  }
}
