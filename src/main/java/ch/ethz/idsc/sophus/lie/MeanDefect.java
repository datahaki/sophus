// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;

public interface MeanDefect {
  /** @param sequence
   * @param weights
   * @param candidate for weighted mean of given sequence
   * @return vector in the direction of true mean */
  Tensor defect(Tensor sequence, Tensor weights, Tensor candidate);
}
