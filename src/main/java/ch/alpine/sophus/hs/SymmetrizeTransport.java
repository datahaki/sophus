// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.13
 * 
 * @see SchildLadder
 * @param hsTransport typically instance of SchildLadder
 * @return */
public record SymmetrizeTransport(HsTransport hsTransport) implements HsTransport, Serializable {
  @Override
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    TensorUnaryOperator shift = hsTransport.shift(p, q);
    return v -> {
      Tensor wp = shift.apply(v);
      Tensor wn = shift.apply(v.negate());
      return wp.subtract(wn).multiply(RationalScalar.HALF);
    };
  }
}
