// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.itp.UniformResample;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.Scalar;

public enum So3UniformResample {
  ;
  /** @param spacing positive
   * @return */
  public static CurveSubdivision of(Scalar spacing) {
    return UniformResample.of(So3Metric.INSTANCE, So3Group.INSTANCE, spacing);
  }
}
