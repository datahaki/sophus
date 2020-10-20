// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.tensor.Tensor;

/** maps a bunch of tangent vectors at a common point to a tensor
 * 
 * @see HsDesign */
@FunctionalInterface
public interface Genesis {
  /** @param levers design matrix
   * @return vector of length of levers */
  Tensor origin(Tensor levers);
}
