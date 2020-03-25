// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum So3AffineCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = HsBarycentricCoordinate.affine( //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log));
}
