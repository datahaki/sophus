// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.MidpointInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Guigui Pennec, p. 6
 * 
 * exact in R^n
 * 
 * @see PoleLadder */
public class SchildLadder implements HsTransport, Serializable {
  /** @param hsManifold
   * @param midpointInterface
   * @return */
  public static HsTransport of(HsManifold hsManifold, MidpointInterface midpointInterface) {
    return new SchildLadder( //
        Objects.requireNonNull(hsManifold), //
        Objects.requireNonNull(midpointInterface));
  }

  /** @param hsManifold
   * @return */
  public static HsTransport of(HsManifold hsManifold) {
    return new SchildLadder(Objects.requireNonNull(hsManifold), null);
  }

  /***************************************************/
  private final HsManifold hsManifold;
  private final MidpointInterface midpointInterface;

  private SchildLadder(HsManifold hsManifold, MidpointInterface midpointInterface) {
    this.hsManifold = hsManifold;
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
      exp_p = hsManifold.exponential(p);
      exp_q = hsManifold.exponential(q);
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
