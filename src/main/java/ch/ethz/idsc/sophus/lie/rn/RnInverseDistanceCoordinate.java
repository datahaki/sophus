// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public enum RnInverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log), InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = new HsBarycentricCoordinate( //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log), InverseNorm.of(RnNormSquared.INSTANCE));
}
