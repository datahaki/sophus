// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

/** StInverseDistanceCoordinate is invariant under left-action */
public enum StInverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBarycentricCoordinate.linear(StManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBarycentricCoordinate.smooth(StManifold.INSTANCE);
}
