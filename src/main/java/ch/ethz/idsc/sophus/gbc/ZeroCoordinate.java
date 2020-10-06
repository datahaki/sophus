// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface ZeroCoordinate {
  /** @param levers
   * @return */
  Tensor fromLevers(Tensor levers);
}
