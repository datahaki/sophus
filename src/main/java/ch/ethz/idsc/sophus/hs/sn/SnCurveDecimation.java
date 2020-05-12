// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.crv.decim.CurveDecimation;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Sign;

public enum SnCurveDecimation {
  ;
  /** @param epsilon
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(SnLineDistance.INSTANCE, Sign.requirePositiveOrZero(epsilon));
  }
}
