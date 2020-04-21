// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** parallel transport in SE(2) covering */
public enum Se2CoveringTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    return uvw -> uvw;
  }
}
