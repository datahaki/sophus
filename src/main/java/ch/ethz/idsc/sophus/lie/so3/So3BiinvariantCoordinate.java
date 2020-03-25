// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

public enum So3BiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(So3Manifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(So3Manifold.INSTANCE);
}
