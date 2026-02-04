// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.JacobiIdentity;

public interface LieAlgebra {
  /** @return tensor of rank 3 satisfying {@link JacobiIdentity} */
  Tensor ad();
}
