// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Guigui Pennec, p. 6 */
/* package */ class SchildLadderExt implements HsTransport, Serializable {
  /** @param hsExponential
   * @return */
  public static HsTransport of(HsExponential hsExponential) {
    return new SchildLadderExt(Objects.requireNonNull(hsExponential));
  }

  /***************************************************/
  private final HsExponential hsExponential;

  private SchildLadderExt(HsExponential hsExponential) {
    this.hsExponential = hsExponential;
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new SchildTransport(xo, xw);
  }

  private class SchildTransport implements TensorUnaryOperator {
    private final Exponential exp_xo;
    private final Tensor xw;
    private final Exponential exp_xw;

    private SchildTransport(Tensor xo, Tensor xw) {
      exp_xo = hsExponential.exponential(xo);
      this.xw = xw;
      exp_xw = hsExponential.exponential(xw);
    }

    @Override
    public Tensor apply(Tensor vo) {
      Tensor xv = exp_xo.exp(vo);
      Exponential exp_xv = hsExponential.exponential(xv);
      // in the literature, mi is computed with respect to xv
      Tensor mi = exp_xv.exp(exp_xv.log(xw).multiply(RationalScalar.HALF));
      Tensor xm = exp_xo.log(mi);
      Tensor xM = exp_xo.exp(xm.add(xm)); // x2a
      return exp_xw.log(xM);
    }
  }
}
