// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.13
 * 
 * @see SchildLadder */
public record SymmetrizeTransport(HsTransport hsTransport) implements HsTransport, Serializable {
  /** @param hsTransport typically instance of SchildLadder
   * @return */
  public SymmetrizeTransport {
    Objects.requireNonNull(hsTransport);
  }

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    return new Rung(hsTransport.shift(orig, dest));
  }

  private static class Rung implements TensorUnaryOperator {
    private final TensorUnaryOperator tensorUnaryOperator;

    public Rung(TensorUnaryOperator tensorUnaryOperator) {
      this.tensorUnaryOperator = tensorUnaryOperator;
    }

    @Override
    public Tensor apply(Tensor vector) {
      Tensor pt1 = tensorUnaryOperator.apply(vector);
      Tensor pt2 = tensorUnaryOperator.apply(vector.negate());
      return pt1.subtract(pt2).multiply(RationalScalar.HALF);
    }
  }
}
