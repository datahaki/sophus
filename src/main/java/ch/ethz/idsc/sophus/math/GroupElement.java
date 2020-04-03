// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;

public interface GroupElement {
  /** @return unique coordinate that represents this group element */
  Tensor toCoordinate();

  /** @return inverse of this element */
  GroupElement inverse();

  /** @param tensor
   * @return group action of this element and the element defined by given tensor */
  Tensor combine(Tensor tensor);
}
