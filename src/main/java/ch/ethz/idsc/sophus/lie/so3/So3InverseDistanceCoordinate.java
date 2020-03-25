// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

/** barycentric coordinates that are invariant under left-action, right-action and inversion */
public enum So3InverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log), InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log), InverseNorm.of(RnNormSquared.INSTANCE));
}
