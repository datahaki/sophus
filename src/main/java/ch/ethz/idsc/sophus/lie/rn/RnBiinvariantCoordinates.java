// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDiagonal;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public enum RnBiinvariantCoordinates {
  ;
  public static final ProjectedCoordinate AFFINE = HsBarycentricCoordinate.affine(RnManifold.INSTANCE);
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(RnManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(RnManifold.INSTANCE);
  // ---
  public static final ProjectedCoordinate DIAGONAL = HsBiinvariantCoordinate.custom( //
      RnManifold.INSTANCE, InverseDiagonal.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate DIAGONAL_SQUARED = HsBiinvariantCoordinate.custom( //
      RnManifold.INSTANCE, InverseDiagonal.of(RnNormSquared.INSTANCE));
}
