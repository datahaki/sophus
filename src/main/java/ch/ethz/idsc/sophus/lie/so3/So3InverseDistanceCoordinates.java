// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

/** barycentric coordinates that are invariant under left-action, right-action and inversion */
public enum So3InverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBarycentricCoordinate.linear(So3Manifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBarycentricCoordinate.smooth(So3Manifold.INSTANCE);
}
