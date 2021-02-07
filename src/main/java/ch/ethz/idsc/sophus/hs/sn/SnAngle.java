// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.sca.ArcCos;
import ch.ethz.idsc.tensor.sca.Clips;

public class SnAngle implements TensorScalarFunction {
  private final Tensor x;

  public SnAngle(Tensor x) {
    this.x = SnMemberQ.INSTANCE.require(x);
  }

  @Override
  public Scalar apply(Tensor y) {
    Scalar xy = (Scalar) x.dot(SnMemberQ.INSTANCE.require(y));
    return ArcCos.FUNCTION.apply(Clips.absoluteOne().apply(xy));
  }
}
