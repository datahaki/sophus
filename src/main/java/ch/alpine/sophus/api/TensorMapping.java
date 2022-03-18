// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

@FunctionalInterface
public interface TensorMapping extends TensorUnaryOperator {
  /** f.slash(list) corresponds to f/@list in Mathematica
   * 
   * @param tensor
   * @return */
  default Tensor slash(Tensor tensor) {
    return Tensor.of(tensor.stream().map(this));
  }
}
