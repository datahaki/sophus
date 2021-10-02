// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public enum Extract2D implements TensorUnaryOperator {
  FUNCTION;

  /** @param tensor
   * @return first two entries of given tensor
   * @throws Exception if given tensor does not contain at least two elements */
  @Override
  public Tensor apply(Tensor tensor) {
    return tensor.extract(0, 2);
  }
}
