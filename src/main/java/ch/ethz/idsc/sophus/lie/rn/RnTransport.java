// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Euclidean vector metric */
public enum RnTransport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    return t -> t;
  }
}
