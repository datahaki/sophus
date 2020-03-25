// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;

public enum SpdInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = //
      new HsInverseDistanceCoordinate(SpdManifold.INSTANCE, InverseDistanceWeighting.of(SpdMetric.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new HsInverseDistanceCoordinate(SpdManifold.INSTANCE, InverseDistanceWeighting.of(SpdMetricSquared.INSTANCE));
}
