// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

/** biinvariant generalized barycentric coordinates */
public enum Se3BiinvariantCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(Se3Manifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(Se3Manifold.INSTANCE);
}
