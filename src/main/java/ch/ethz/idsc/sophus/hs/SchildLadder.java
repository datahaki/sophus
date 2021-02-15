// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Guigui Pennec, p. 6
 * 
 * exact in R^n
 * 
 * @see PoleLadder */
public class SchildLadder implements HsTransport, Serializable {
  /** @param hsExponential
   * @param midpointInterface
   * @return */
  public static HsTransport of(HsExponential hsExponential, MidpointInterface midpointInterface) {
    return new SchildLadder( //
        Objects.requireNonNull(hsExponential), //
        Objects.requireNonNull(midpointInterface));
  }

  /** @param hsExponential
   * @return */
  public static HsTransport of(HsExponential hsExponential) {
    return new SchildLadder(Objects.requireNonNull(hsExponential), null);
  }

  /***************************************************/
  private final HsExponential hsExponential;
  private final MidpointInterface midpointInterface;

  private SchildLadder(HsExponential hsExponential, MidpointInterface midpointInterface) {
    this.hsExponential = hsExponential;
    this.midpointInterface = midpointInterface;
  }

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Rung(p, q);
  }

  private class Rung implements TensorUnaryOperator {
    private final Tensor q;
    private final Exponential exp_p;
    private final Exponential exp_q;

    private Rung(Tensor p, Tensor q) {
      this.q = q;
      exp_p = hsExponential.exponential(p);
      exp_q = hsExponential.exponential(q);
    }

    @Override
    public Tensor apply(Tensor v) {
      Tensor x = exp_p.exp(v);
      Tensor m = Objects.isNull(midpointInterface) //
          ? HsMidpoint.of(exp_q, x)
          : midpointInterface.midpoint(q, x);
      Tensor xm = exp_p.log(m);
      return exp_q.log(exp_p.exp(xm.add(xm)));
    }
  }
}
