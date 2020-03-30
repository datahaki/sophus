// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;

/* package */ enum StaticHelper {
  ;
  private static final Chop CHOP = Chop._10;

  /** @param x
   * @return */
  public static Tensor requirePoint(Tensor x) {
    Scalar xn = Last.of(x);
    Sign.requirePositive(xn);
    if (!Scalars.lessEquals(RealScalar.ONE, xn))
      throw TensorRuntimeException.of(x, xn);
    CHOP.requireClose(HnNormSquared.INSTANCE.norm(x), RealScalar.ONE.negate());
    return x;
  }

  /** @param x
   * @param v
   * @return */
  public static Tensor requireTangent(Tensor x, Tensor v) {
    CHOP.requireZero(HnBilinearForm.between(x, v));
    return v;
  }
}
