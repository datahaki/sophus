// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.red.VectorAngle;

/**  */
public enum RpnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    Scalar d_xy = VectorAngle.of(x, y).get();
    return Min.of(d_xy, Pi.VALUE.subtract(d_xy));
  }
}
