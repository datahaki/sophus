// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.Scalar;

public enum Se2CurveDecimation {
  ;
  /** @param epsilon non-negative
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(Se2Group.INSTANCE, epsilon);
  }
}
