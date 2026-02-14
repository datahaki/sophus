// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.math.UpperVectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

enum SoExponential implements Exponential {
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
  public TensorUnaryOperator vectorLog() {
    return q -> UpperVectorize.of(q, 1);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSoMemberQ.INSTANCE;
  }
}
