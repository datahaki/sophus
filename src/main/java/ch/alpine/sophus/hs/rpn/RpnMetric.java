// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.VectorAngle;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Min;

/** real projective plane */
public enum RpnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    Scalar d_xy = VectorAngle.of(x, y).orElseThrow();
    return Min.of(d_xy, Pi.VALUE.subtract(d_xy));
  }
}
