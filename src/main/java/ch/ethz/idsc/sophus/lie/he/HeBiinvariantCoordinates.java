// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

public enum HeBiinvariantCoordinates {
  ;
  public static final ProjectedCoordinate AFFINE = HsBarycentricCoordinate.affine(HeManifold.INSTANCE);
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(HeManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(HeManifold.INSTANCE);
}
