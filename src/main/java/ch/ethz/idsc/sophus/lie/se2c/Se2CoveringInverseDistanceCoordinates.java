// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.r2.Se2CoveringParametricDistance;
import ch.ethz.idsc.sophus.math.win.InverseDistanceCoordinates;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se2CoveringInverseDistanceCoordinates {
  ;
  // TODO this is not the best default value!
  private static final TensorUnaryOperator INVERSE_NORM = //
      InverseNorm.of(Se2CoveringParametricDistance.INSTANCE);
  /** @param tensor of coordinates in Lie group
   * @return */
  public static final InverseDistanceCoordinates INSTANCE = new LieInverseDistanceCoordinates( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringBarycenter::equation, // FIXME this is probably not confirmed !?!
      INVERSE_NORM);
}
