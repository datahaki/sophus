// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ex.MatrixExp;

/** @see MatrixExp */
public interface MatrixGroup {
  /** @return basis for the algebra! */
  Tensor matrixBasis();
}
