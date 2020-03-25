// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;

public class SpdInverseDistanceCoordinate extends HsInverseDistanceCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SpdInverseDistanceCoordinate(InverseDistanceWeighting.of(SpdMetric.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SpdInverseDistanceCoordinate(InverseDistanceWeighting.of(SpdMetricSquared.INSTANCE));

  public SpdInverseDistanceCoordinate(BarycentricCoordinate target) {
    super(SpdManifold.INSTANCE, target);
  }
}
