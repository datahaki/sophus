// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is invariant under left-action */
public enum Se2CoveringInverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log), InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log), InverseNorm.of(RnNormSquared.INSTANCE));
  public static final ProjectedCoordinate AD_INVAR = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log),
      InverseNorm.of(new Se2CoveringTarget(RnNormSquared.INSTANCE, RealScalar.ONE)));
}
