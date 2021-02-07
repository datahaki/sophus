// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;

/** Exp_e[X] and Log_e[M]
 * 
 * input X is a square matrix
 * input M is an invertible matrix */
public enum GlnExponential implements Exponential {
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
