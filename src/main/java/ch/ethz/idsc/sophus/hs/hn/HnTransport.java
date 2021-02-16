// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** several enhancements over the pole ladder:
 * faster midpoint and flip computation */
public enum HnTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Rung(p, q);
  }

  private class Rung implements TensorUnaryOperator {
    private final HnExponential exp_p;
    private final HnExponential exp_q;
    private final HnExponential exp_m;

    private Rung(Tensor p, Tensor q) {
      exp_p = new HnExponential(p);
      exp_q = new HnExponential(q);
      Tensor m = HnGeodesic.INSTANCE.midpoint(p, q);
      exp_m = new HnExponential(m);
    }

    @Override
    public Tensor apply(Tensor v) {
      return exp_q.log(exp_m.flip(exp_p.exp(v))).negate();
    }
  }
}
