// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;

public enum So3BiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log), RnNorm.INSTANCE);
  public static final ProjectedCoordinate SQUARED = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log), RnNormSquared.INSTANCE);
}
