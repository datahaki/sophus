// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.13
 * 
 * @see SchildLadder */
public class SymmetrizeTransport implements HsTransport, Serializable {
  /** @param hsTransport typically instance of SchildLadder
   * @return */
  public static HsTransport of(HsTransport hsTransport) {
    return new SymmetrizeTransport(Objects.requireNonNull(hsTransport));
  }

  /***************************************************/
  private final HsTransport hsTransport;

  private SymmetrizeTransport(HsTransport hsTransport) {
    this.hsTransport = hsTransport;
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
