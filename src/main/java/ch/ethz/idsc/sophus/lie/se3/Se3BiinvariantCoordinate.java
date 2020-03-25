// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

/** biinvariant generalized barycentric coordinates */
public enum Se3BiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = HsBiinvariantCoordinate.linear( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog));
  public static final ProjectedCoordinate SQUARED = HsBiinvariantCoordinate.smooth( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog));
}
