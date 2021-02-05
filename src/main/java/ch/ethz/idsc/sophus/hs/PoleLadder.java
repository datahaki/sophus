// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** the pole ladder is exact in symmetric spaces
 * 
 * References:
 * "Efficient Parallel Transport of Deformations in Time Series of Images:
 * from Schild’s to Pole Ladder"
 * by Marco Lorenzi, Xavier Pennec, 2013
 * 
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.14
 * 
 * @see SchildLadder */
public class PoleLadder implements HsTransport, Serializable {
  /** @param hsExponential
   * @param midpointInterface
   * @return */
  public static HsTransport of(HsExponential hsExponential, MidpointInterface midpointInterface) {
    return new PoleLadder( //
        Objects.requireNonNull(hsExponential), //
        Objects.requireNonNull(midpointInterface));
  }

  /** @param hsExponential
   * @return */
  public static HsTransport of(HsExponential hsExponential) {
    return new PoleLadder(Objects.requireNonNull(hsExponential), null);
  }

  /***************************************************/
  private final HsExponential hsExponential;
  private final MidpointInterface midpointInterface;

  private PoleLadder(HsExponential hsExponential, MidpointInterface midpointInterface) {
    this.hsExponential = hsExponential;
    this.midpointInterface = midpointInterface;
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new Rung(xo, xw);
  }

  private class Rung implements TensorUnaryOperator {
    private final Exponential exp_xo;
    private final Exponential exp_xw;
    private final Exponential exp_mi;

    private Rung(Tensor xo, Tensor xw) {
      exp_xo = hsExponential.exponential(xo);
      exp_xw = hsExponential.exponential(xw);
      Tensor mi = Objects.isNull(midpointInterface) //
          ? HsMidpoint.of(exp_xo, xw)
          : midpointInterface.midpoint(xo, xw);
      exp_mi = hsExponential.exponential(mi);
    }

    @Override
    public Tensor apply(Tensor vo) {
      return exp_xw.log(exp_mi.exp(exp_mi.log(exp_xo.exp(vo)).negate())).negate();
    }
  }
}
