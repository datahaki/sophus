// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;

public enum SpdInverseDistanceCoordinates {
  ;
  public static final BarycentricCoordinate LINEAR = //
      new HsInverseDistanceCoordinate(SpdManifold.INSTANCE, InverseDistanceWeighting.of(SpdMetric.INSTANCE));
  public static final BarycentricCoordinate SMOOTH = //
      new HsInverseDistanceCoordinate(SpdManifold.INSTANCE, InverseDistanceWeighting.of(SpdMetricSquared.INSTANCE));
}
