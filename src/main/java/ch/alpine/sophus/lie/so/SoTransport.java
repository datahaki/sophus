// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.re.Inverse;

/** Reference:
 * "Hermite subdivision on manifolds via parallel transport"
 * by Caroline Moosmueller, 2017 */
public enum SoTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor qpinv = q.dot(Inverse.of(p)); // "move vector from p to e then move to q"
    return vp -> qpinv.dot(vp);
  }
}
