// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.sca.Chop;

/** Reference:
 * "Hermite subdivision on manifolds via parallel transport"
 * by Caroline Moosmueller, 2017 */
public enum SonTransport implements HsTransport {
  INSTANCE;

  private static final HsMemberQ HS_MEMBER_Q = SonMemberQ.of(Chop._08);

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor qpinv = q.dot(Inverse.of(p));
    return vp -> qpinv.dot(HS_MEMBER_Q.requireTangent(p, vp));
  }
}
