// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.itp.UniformResample;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.Scalar;

public enum RnUniformResample {
  ;
  /** @param spacing positive
   * @return */
  public static CurveSubdivision of(Scalar spacing) {
    return UniformResample.of(RnMetric.INSTANCE, RnGroup.INSTANCE, spacing);
  }
}
