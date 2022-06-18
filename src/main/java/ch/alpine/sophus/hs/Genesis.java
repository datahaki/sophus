// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;

/** maps a bunch of tangent vectors at a common point to a tensor */
@FunctionalInterface
public interface Genesis extends Serializable {
  /** @param levers, or design matrix
   * @return vector with length equals to number of levers */
  Tensor origin(Tensor levers);
}
