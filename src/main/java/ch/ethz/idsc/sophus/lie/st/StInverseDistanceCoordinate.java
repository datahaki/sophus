// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

/** StInverseDistanceCoordinate is invariant under left-action */
public enum StInverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog), InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog), InverseNorm.of(RnNormSquared.INSTANCE));
}
