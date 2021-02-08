// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.Inverse;

/** Reference:
 * "Hermite subdivision on manifolds via parallel transport"
 * by Caroline Moosmueller, 2017 */
public enum SonTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor qpinv = q.dot(Inverse.of(p)); // "move vector from p to e then move to q"
    return vp -> qpinv.dot(vp);
  }
}
