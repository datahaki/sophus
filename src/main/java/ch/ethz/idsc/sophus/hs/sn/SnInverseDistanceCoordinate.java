// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

/** On S^n the inverse distance coordinate is determined explicitly, whereas
 * the weighted average is approximated iteratively.
 * 
 * Given a scattered sequence of sufficiently distributed points and a point on S^n,
 * then SnInverseDistanceCoordinate.weights(sequence, point) gives a weight vector
 * that satisfies SnMean(sequence, weights) == point.
 * 
 * @see SnMean */
public enum SnInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = //
      new HsBarycentricCoordinate(SnManifold.INSTANCE, InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new HsBarycentricCoordinate(SnManifold.INSTANCE, InverseNorm.of(RnNormSquared.INSTANCE));
}
