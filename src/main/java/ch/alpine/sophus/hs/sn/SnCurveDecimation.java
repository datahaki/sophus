// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.sca.Sign;

public enum SnCurveDecimation {
  ;
  /** @param epsilon
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(SnLineDistance.INSTANCE, Sign.requirePositiveOrZero(epsilon));
  }
}
