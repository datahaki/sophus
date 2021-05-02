// code by jph
package ch.alpine.sophus.math;

import java.util.function.Predicate;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;

/** membership status of given tensor
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MemberQ.html">MemberQ</a> */
@FunctionalInterface
public interface MemberQ extends Predicate<Tensor> {
  /** @param tensor
   * @return tensor
   * @throws Exception if given tensor does not have membership status */
  default Tensor require(Tensor tensor) {
    if (test(tensor))
      return tensor;
    throw TensorRuntimeException.of(tensor);
  }
}
