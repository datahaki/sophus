// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Guigui Pennec, p. 6
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

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new Rung(xo, xw);
  }

  private class Rung implements TensorUnaryOperator {
    private final Tensor xw;
    private final Exponential exp_xo;
    private final Exponential exp_xw;

    private Rung(Tensor xo, Tensor xw) {
      this.xw = xw;
      exp_xo = hsExponential.exponential(xo);
      exp_xw = hsExponential.exponential(xw);
    }

    @Override
    public Tensor apply(Tensor vo) {
      Tensor xv = exp_xo.exp(vo);
      Tensor mi = Objects.isNull(midpointInterface) //
          ? HsMidpoint.of(exp_xw, xv)
          : midpointInterface.midpoint(xw, xv);
      Tensor xm = exp_xo.log(mi);
      return exp_xw.log(exp_xo.exp(xm.add(xm)));
    }
  }
}
