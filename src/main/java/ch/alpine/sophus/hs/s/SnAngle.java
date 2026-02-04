// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.ArcCos;

/** maps a point q to the (angular) distance from base point p */
public record SnAngle(Tensor x) implements TensorScalarFunction {
  public SnAngle {
    SnManifold.INSTANCE.requireMember(x);
  }

  @Override
  public Scalar apply(Tensor y) {
    Scalar xy = (Scalar) x.dot(SnManifold.INSTANCE.requireMember(y));
    return ArcCos.FUNCTION.apply(Clips.absoluteOne().apply(xy));
  }
}
