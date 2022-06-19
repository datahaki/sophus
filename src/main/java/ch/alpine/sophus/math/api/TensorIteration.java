// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface TensorIteration {
  /** @return result of next step of tensor iteration */
  Tensor iterate();
}
