// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.math.win.InverseDiagonal;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public enum RnBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate LINEAR = HsBiinvariantCoordinate.linear(RnManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBiinvariantCoordinate.smooth(RnManifold.INSTANCE);
  // ---
  public static final ProjectedCoordinate DIAGONAL = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log), InverseDiagonal.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate DIAGONAL_SQUARED = new HsBiinvariantCoordinate( //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log), InverseDiagonal.of(RnNormSquared.INSTANCE));
}
