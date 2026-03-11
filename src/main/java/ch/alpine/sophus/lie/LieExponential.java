// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;

public interface LieExponential extends Exponential {
  /** @param x member of tangent space at neutral element {@link #isTangentQ()}
   * @return matrix in the corresponding lie algebra */
  default Tensor gl_representation(Tensor x) {
    return SquareMatrixQ.INSTANCE.require(isTangentQ().require(x));
  }
}
