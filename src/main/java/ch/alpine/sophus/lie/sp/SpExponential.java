// code by jph
package ch.alpine.sophus.lie.sp;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

enum SpExponential implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return MatrixExp.of(matrix);
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override // from Exponential
  public ZeroDefectArrayQ isTangentQ() {
    return TSpMemberQ.INSTANCE;
  }
}
