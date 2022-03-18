// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface TensorIteration {
  /** @return result of next step of tensor iteration */
  Tensor iterate();
}
