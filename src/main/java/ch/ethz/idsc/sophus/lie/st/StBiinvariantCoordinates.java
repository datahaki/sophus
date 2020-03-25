// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

public enum StBiinvariantCoordinates {
  ;
  public static final ProjectedCoordinate AFFINE = HsBarycentricCoordinate.affine(StManifold.INSTANCE);
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(StManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(StManifold.INSTANCE);
}
