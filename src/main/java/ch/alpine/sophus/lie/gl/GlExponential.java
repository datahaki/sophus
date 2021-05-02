// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.lie.MatrixExp;
import ch.alpine.tensor.lie.MatrixLog;

/** Exp_e[X] and Log_e[M]
 * 
 * input X is a square matrix
 * input M is an invertible matrix */
public enum GlExponential implements Exponential {
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
    return Flatten.of(log(matrix));
  }
}
