// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import ch.ethz.idsc.sophus.decim.CurveDecimation;
import ch.ethz.idsc.tensor.Scalar;

public enum Se2CurveDecimation {
  ;
  /** @param epsilon non-negative
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(Se2Manifold.INSTANCE, epsilon);
  }
}
