// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.ArcCos;

/** maps a point q to the (angular) distance from base point p */
public record SnAngle(Tensor x) implements TensorScalarFunction {
  public SnAngle {
    SnMemberQ.INSTANCE.require(x);
  }

  @Override
  public Scalar apply(Tensor y) {
    Scalar xy = (Scalar) x.dot(SnMemberQ.INSTANCE.require(y));
    return ArcCos.FUNCTION.apply(Clips.absoluteOne().apply(xy));
  }
}
