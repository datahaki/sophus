// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum RnInverseDistanceCoordinates {
  ;
  /** @param tensor of coordinates in Lie group
   * @return */
  public static TensorUnaryOperator of(Tensor tensor) {
    return new LieInverseDistanceCoordinates( //
        RnGroup.INSTANCE, //
        RnExponential.INSTANCE::log, //
        InverseNorm.of(RnVectorNorm.INSTANCE)).of(tensor);
  }
}
