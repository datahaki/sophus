// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.r2.Se2CoveringParametricDistance;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public class Se2CoveringInverseDistanceCoordinates {
  // TODO this is not the best default value!
  private static final TensorUnaryOperator INVERSE_NORM = //
      InverseNorm.of(Se2CoveringParametricDistance.INSTANCE);

  /** @param tensor of coordinates in Lie group
   * @return */
  public static TensorUnaryOperator of(Tensor tensor) {
    return new LieInverseDistanceCoordinates( //
        Se2CoveringGroup.INSTANCE, //
        Se2CoveringBarycenter::equation, //
        INVERSE_NORM).of(tensor);
  }
}
