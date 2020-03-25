// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;

/** biinvariant generalized barycentric coordinates */
public enum Se3BiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog), RnNorm.INSTANCE);
  public static final ProjectedCoordinate SQUARED = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog), RnNormSquared.INSTANCE);
}
