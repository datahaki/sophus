// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.win.InverseDistanceCoordinates;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

/** @see RnBiinvariantMean */
public enum RnBarycenter {
  ;
  /** @param sequence
   * @return */
  public static TensorUnaryOperator of(Tensor sequence) {
    return InverseDistanceCoordinates.of(Norm._2::ofVector, sequence);
  }
}
