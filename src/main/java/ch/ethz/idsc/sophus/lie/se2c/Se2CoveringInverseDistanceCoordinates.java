// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is invariant under left-action */
public enum Se2CoveringInverseDistanceCoordinates {
  ;
  public static final ProjectedCoordinate LINEAR = HsBarycentricCoordinate.linear(Se2CoveringManifold.INSTANCE);
  public static final ProjectedCoordinate SMOOTH = HsBarycentricCoordinate.smooth(Se2CoveringManifold.INSTANCE);
  public static final ProjectedCoordinate AD_INVAR = HsBarycentricCoordinate.custom( //
      Se2CoveringManifold.INSTANCE, //
      InverseNorm.of(new Se2CoveringTarget(RnNormSquared.INSTANCE, RealScalar.ONE)));
}
