// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** several enhancements over the pole ladder:
 * faster midpoint and flip computation */
public enum GrTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Rung(p, q);
  }

  private class Rung implements TensorUnaryOperator {
    private final GrExponential exp_p;
    private final GrExponential exp_q;
    private final GrExponential exp_m;

    private Rung(Tensor p, Tensor q) {
      exp_p = new GrExponential(p);
      exp_q = new GrExponential(q);
      Tensor m = exp_p.midpoint(q);
      exp_m = new GrExponential(m);
    }

    @Override
    public Tensor apply(Tensor v) {
      return exp_q.log(exp_m.flip(exp_p.exp(v))).negate();
    }
  }
}
