// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public enum S2Transport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    return RotationMatrix3D.of(orig, dest)::dot;
  }
}
