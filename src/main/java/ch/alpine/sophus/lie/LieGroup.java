// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;

/** interface maps tensor coordinate to an element of a lie group */
public interface LieGroup {
  /** function produces an instance of a lie group element from a given tensor
   * 
   * @param tensor
   * @return lie group element */
  LieGroupElement element(Tensor tensor);

  /** @return exponential at neutral element */
  Exponential exponential();
  // TODO SOPHUS API should give LieAlgebra with ad (since this is implied from basis/group action)
}
