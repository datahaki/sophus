// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

@FunctionalInterface
public interface MemberQ {
  /** @param element
   * @return membership status of given element */
  boolean isMember(Tensor tensor);

  /** @param tensor
   * @return */
  default Tensor require(Tensor tensor) {
    if (isMember(tensor))
      return tensor;
    throw TensorRuntimeException.of(tensor);
  }
}
