// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

public enum SnBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(SnManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(SnManifold.INSTANCE);
}
