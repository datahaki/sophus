// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.red.VectorAngle;

/** real projective plane */
public enum RpnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    Scalar d_xy = VectorAngle.of(x, y).get();
    return Min.of(d_xy, Pi.VALUE.subtract(d_xy));
  }
}
