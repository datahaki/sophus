// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.itp.UniformResample;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.Scalar;

public enum SnUniformResample {
  ;
  /** @param spacing positive
   * @return */
  public static CurveSubdivision of(Scalar spacing) {
    return UniformResample.of(SnMetric.INSTANCE, SnGeodesic.INSTANCE, spacing);
  }
}
