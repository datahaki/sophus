// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.Scalar;

public enum Se3CurveDecimation {
  ;
  /** @param epsilon
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(Se3Manifold.INSTANCE, epsilon);
  }
}
