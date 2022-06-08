// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.itp.UniformResample;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.Scalar;

public enum Se2UniformResample {
  ;
  /** @param spacing positive
   * @return */
  public static CurveSubdivision of(Scalar spacing) {
    return UniformResample.of(Se2Parametric.INSTANCE, Se2Group.INSTANCE, spacing);
  }
}
