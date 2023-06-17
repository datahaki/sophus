// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Guigui Pennec, p. 6
 * 
 * exact in R^n
 * 
 * @see PoleLadder */
public record SchildLadder(HomogeneousSpace homogeneousSpace) implements HsTransport, Serializable {

  public SchildLadder {
    Objects.requireNonNull(homogeneousSpace);
  }

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Rung(p, q);
  }
  private class Rung implements TensorUnaryOperator {
    private final Exponential exp_p;
    private final Exponential exp_q;

    private Rung(Tensor p, Tensor q) {
      exp_p = homogeneousSpace.exponential(p);
      exp_q = homogeneousSpace.exponential(q);
    }

    @Override
    public Tensor apply(Tensor v) {
      Tensor x = exp_p.exp(v);
      Tensor m = exp_q.midpoint(x);
      Tensor xm = exp_p.log(m);
      return exp_q.log(exp_p.exp(xm.add(xm)));
    }
  }
}
