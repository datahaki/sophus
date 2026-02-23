// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface TensorDistance {
  Scalar distance(Tensor r);
}
