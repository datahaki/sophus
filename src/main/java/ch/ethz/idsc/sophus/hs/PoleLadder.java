package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** References:
 * "Efficient Parallel Transport of Deformations in Time Series of Images:
 * from Schild’s to Pole Ladder"
 * by Marco Lorenzi, Xavier Pennec, 2013
 * 
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.14 */
public class PoleLadder implements HsTransport, Serializable {
  /** @param hsExponential
   * @return */
  public static HsTransport of(HsExponential hsExponential) {
    return new PoleLadder(Objects.requireNonNull(hsExponential));
  }

  /***************************************************/
  private final HsExponential hsExponential;

  private PoleLadder(HsExponential hsExponential) {
    this.hsExponential = hsExponential;
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new PoleTransport(xo, xw);
  }

  private class PoleTransport implements TensorUnaryOperator {
    private final Exponential exp_xo;
    private final Exponential exp_xw;
    private final Exponential exp_mi;

    private PoleTransport(Tensor xo, Tensor xw) {
      exp_xo = hsExponential.exponential(xo);
      exp_xw = hsExponential.exponential(xw);
      Tensor mi = exp_xo.exp(exp_xo.log(xw).multiply(RationalScalar.HALF));
      exp_mi = hsExponential.exponential(mi);
    }

    @Override
    public Tensor apply(Tensor vo) {
      Tensor xv = exp_xo.exp(vo);
      Tensor a = exp_mi.log(xv);
      Tensor z = exp_mi.exp(a.negate()); // x2a
      return exp_xw.log(z).negate();
    }
  }
}
