// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.hs.r2.Se2CoveringParametricDistance;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBarycenter;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public class Se2CoveringIdfCoordinates {
  private static final TensorUnaryOperator INVERSE_NORM = //
      InverseNorm.of(Se2CoveringParametricDistance.INSTANCE);

  public static TensorUnaryOperator of(Tensor sequence) {
    return new LieInverseDistanceCoordinates( //
        Se2CoveringGroup.INSTANCE, //
        Se2CoveringBarycenter::equation, //
        INVERSE_NORM).of(sequence);
  }
}
