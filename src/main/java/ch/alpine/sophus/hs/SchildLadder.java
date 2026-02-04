// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Guigui Pennec, p. 6
 * 
 * exact in R^n
 * 
 * @see PoleLadder */
public record SchildLadder(HomogeneousSpace homogeneousSpace) implements HsTransport, Serializable {
  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Exponential exp_p = homogeneousSpace.exponential(p);
    Exponential exp_q = homogeneousSpace.exponential(q);
    return v -> {
      Tensor x = exp_p.exp(v);
      Tensor m = homogeneousSpace.midpoint(q, x);
      Tensor xm = exp_p.log(m);
      return exp_q.log(exp_p.exp(xm.add(xm)));
    };
  }
}
