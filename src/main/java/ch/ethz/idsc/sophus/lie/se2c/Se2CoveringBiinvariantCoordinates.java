// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;

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
public enum Se2CoveringBiinvariantCoordinates {
  ;
  public static final ProjectedCoordinate AFFINE = HsBarycentricCoordinate.affine(Se2CoveringManifold.INSTANCE);
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(Se2CoveringManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(Se2CoveringManifold.INSTANCE);
}
