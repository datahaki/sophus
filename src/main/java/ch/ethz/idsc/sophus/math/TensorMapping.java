// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
