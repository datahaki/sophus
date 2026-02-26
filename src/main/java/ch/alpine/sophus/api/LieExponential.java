// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;

public interface LieExponential extends Exponential {
  /** @param x
   * @return matrix in the corresponding lie algebra */
  default Tensor gl_representation(Tensor x) {
    return SquareMatrixQ.INSTANCE.require(isTangentQ().require(x));
  }
}
