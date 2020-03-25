// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

public enum HeBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(HeManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(HeManifold.INSTANCE);
}
