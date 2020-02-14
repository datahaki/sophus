// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.r2.Se2CoveringParametricDistance;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se2CoveringInverseDistanceCoordinate {
  ;
  // TODO this is not the best default value!
  private static final TensorUnaryOperator INVERSE_NORM = //
      InverseNorm.of(Se2CoveringParametricDistance.INSTANCE);
  /** @param tensor of coordinates in Lie group
   * @return */
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringBarycenter::equation, // FIXME this is probably not confirmed !?!
      INVERSE_NORM);
}
