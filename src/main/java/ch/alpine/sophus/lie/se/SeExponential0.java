// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

enum SeExponential0 implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return MatrixExp.of(matrix);
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSeMemberQ.INSTANCE;
  }
}
