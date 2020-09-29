// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;

/** Reference:
 * "Hermite subdivision on manifolds via parallel transport"
 * by Caroline Moosmueller, 2017 */
public enum SonTransport implements HsTransport {
  INSTANCE;

  private static final MemberQ MEMBER_Q = SonMemberQ.of(Chop._08);

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor qpinv = q.dot(Inverse.of(p));
    return vp -> qpinv.dot(MEMBER_Q.requireTangent(p, vp));
  }
}