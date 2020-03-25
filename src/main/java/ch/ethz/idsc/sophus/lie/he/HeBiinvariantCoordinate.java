// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;

public enum HeBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog), RnNorm.INSTANCE);
  public static final ProjectedCoordinate SQUARED = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog), RnNormSquared.INSTANCE);
}
