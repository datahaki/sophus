// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public enum RnInverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBarycentricCoordinate.linear(RnManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBarycentricCoordinate.smooth(RnManifold.INSTANCE);
}
