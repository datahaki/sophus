// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.crv.decim.CurveDecimation;
import ch.ethz.idsc.tensor.Scalar;

public enum Se3CurveDecimation {
  ;
  /** @param epsilon
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(Se3Manifold.INSTANCE, epsilon);
  }
}
