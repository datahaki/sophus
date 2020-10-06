// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.tensor.Tensor;

/** @see HsDesign */
@FunctionalInterface
public interface Genesis {
  /** @param levers design matrix
   * @return vector of length of levers */
  Tensor origin(Tensor levers);
}
