// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/MemberQ.html">MemberQ</a> */
@FunctionalInterface
public interface MemberQ {
  /** @param tensor
   * @return membership status of given tensor */
  boolean isMember(Tensor tensor);

  /** @param tensor
   * @return tensor
   * @throws Exception if given tensor does not have membership status */
  default Tensor require(Tensor tensor) {
    if (isMember(tensor))
      return tensor;
    throw TensorRuntimeException.of(tensor);
  }
}
