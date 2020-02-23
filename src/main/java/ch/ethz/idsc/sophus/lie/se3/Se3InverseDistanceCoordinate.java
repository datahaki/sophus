// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se3InverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieBarycentricCoordinate( //
      Se3Group.INSTANCE, //
      Se3Exponential::flattenLog, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new LieBarycentricCoordinate( //
      Se3Group.INSTANCE, //
      Se3Exponential::flattenLog, //
      InverseNorm.of(RnNormSquared.INSTANCE));
}
