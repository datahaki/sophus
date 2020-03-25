// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

/** left-invariant generalized barycentric coordinates
 * 
 * given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * @see Se3BiinvariantCoordinates */
public enum Se3InverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBarycentricCoordinate.linear(Se3Manifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBarycentricCoordinate.smooth(Se3Manifold.INSTANCE);
}
