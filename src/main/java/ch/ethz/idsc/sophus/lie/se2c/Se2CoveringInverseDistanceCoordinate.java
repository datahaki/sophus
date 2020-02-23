// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is invariant under left-action */
public enum Se2CoveringInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringExponential.INSTANCE::log, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new LieInverseDistanceCoordinate( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringExponential.INSTANCE::log, //
      InverseNorm.of(RnNormSquared.INSTANCE));
}
