// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.win.InverseDistanceCoordinates;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum RnInverseDistanceCoordinates {
  ;
  public static final InverseDistanceCoordinates INSTANCE = new LieInverseDistanceCoordinates( //
      RnGroup.INSTANCE, //
      RnExponential.INSTANCE::log, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final InverseDistanceCoordinates SQUARED = new LieInverseDistanceCoordinates( //
      RnGroup.INSTANCE, //
      RnExponential.INSTANCE::log, //
      InverseNorm.of(RnNormSquared.INSTANCE));
}
