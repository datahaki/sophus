// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

/** left-invariant generalized barycentric coordinates
 * 
 * given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * @see Se3BiinvariantCoordinate */
public enum Se3InverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog), InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog), InverseNorm.of(RnNormSquared.INSTANCE));
}
