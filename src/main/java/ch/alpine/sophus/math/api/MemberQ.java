// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;

/** membership status of given tensor
 * 
 * If an enum implements this interface in a singleton pattern, then
 * the use of the enum constant INSTANCE is recommended.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MemberQ.html">MemberQ</a> */
@FunctionalInterface
public interface MemberQ extends Region<Tensor> {
  /** @param tensor
   * @return tensor
   * @throws Exception if given tensor does not have membership status */
  default Tensor require(Tensor tensor) {
    if (test(tensor))
      return tensor;
    throw Throw.of(tensor);
  }
}
