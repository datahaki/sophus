// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface Genesis {
  /** @param levers
   * @return */
  Tensor origin(Tensor levers);
}
