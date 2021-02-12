// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** the pole ladder is exact in symmetric spaces */
public enum GrTransport implements HsTransport {
  INSTANCE;

  private static final HsTransport POLE_LADDER = PoleLadder.of(GrManifold.INSTANCE);

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    TensorUnaryOperator shift = POLE_LADDER.shift(p, q);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    return pv -> shift.apply(tGrMemberQ.require(pv));
  }
}
