// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;

/** maps a bunch of tangent vectors at a common point to a tensor */
@FunctionalInterface
public interface Genesis {
  /** @param levers, or design matrix
   * @return vector with length equals to number of levers */
  Tensor origin(Tensor levers);
}
