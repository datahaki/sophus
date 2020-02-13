// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** @see RnBiinvariantMean */
public enum RnBarycenter {
  ;
  /** @param sequence
   * @return */
  public static TensorUnaryOperator of(Tensor sequence) {
    Objects.requireNonNull(sequence);
    return p -> RnInverseDistanceCoordinates.INSTANCE.weights(sequence, p);
  }
}
