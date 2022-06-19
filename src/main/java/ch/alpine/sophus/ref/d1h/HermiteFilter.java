// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.math.api.TensorIteration;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public interface HermiteFilter {
  /** @param delta between two samples in control points
   * @param control
   * @return */
  TensorIteration string(Scalar delta, Tensor control);
}
