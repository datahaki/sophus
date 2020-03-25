// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

public enum ScInverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate INSTANCE = //
      HsBarycentricCoordinate.custom(ScManifold.INSTANCE, InverseNorm.of(ScVectorNorm.INSTANCE));
}
