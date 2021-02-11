// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** the pole ladder is exact in symmetric spaces
 * 
 * Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * Nicolas Guigui, Xavier Pennec, 2020 */
public enum HnTransport implements HsTransport {
  INSTANCE;

  private static final HsTransport POLE_LADDER = PoleLadder.of(HnManifold.INSTANCE);

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    TensorUnaryOperator shift = POLE_LADDER.shift(p, q);
    THnMemberQ tHnMemberQ = new THnMemberQ(p);
    return pv -> shift.apply(tHnMemberQ.require(pv));
  }
}
