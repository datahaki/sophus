// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.itp.UniformResample;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.Scalar;

public enum So2UniformResample {
  ;
  /** @param spacing positive
   * @return */
  public static CurveSubdivision of(Scalar spacing) {
    return UniformResample.of(So2Metric.INSTANCE, So2Geodesic.INSTANCE, spacing);
  }
}
