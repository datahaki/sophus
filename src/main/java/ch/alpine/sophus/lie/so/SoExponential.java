// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.math.LowerVectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

/** SO(n) group of orthogonal matrices with determinant +1 */
public enum SoExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return MatrixExp.of(matrix);
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor matrix) {
    return LowerVectorize.of(log(matrix), -1);
  }
}
