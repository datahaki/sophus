// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

/** On S^n the inverse distance coordinate is determined explicitly, whereas
 * the weighted average is approximated iteratively.
 * 
 * Given a scattered sequence of sufficiently distributed points and a point on S^n,
 * then SnInverseDistanceCoordinate.weights(sequence, point) gives a weight vector
 * that satisfies SnMean(sequence, weights) == point.
 * 
 * @see SnMean */
public enum SnInverseDistanceCoordinates {
  ;
  public static final BarycentricCoordinate LINEAR = HsBarycentricCoordinate.linear(SnManifold.INSTANCE);
  public static final BarycentricCoordinate SMOOTH = HsBarycentricCoordinate.smooth(SnManifold.INSTANCE);
}
