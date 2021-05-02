// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.Vectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.MatrixExp;
import ch.alpine.tensor.lie.MatrixLog;

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
    return Vectorize.of(log(matrix), -1);
  }
}
