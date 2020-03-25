// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is
 * 
 * linear reproduction
 * lagrange property
 * biinvariant
 * smooth for beta == 2 */
public enum Se2CoveringBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log), RnNorm.INSTANCE);
  public static final ProjectedCoordinate SQUARED = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log), RnNormSquared.INSTANCE);
}
