// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.math.UpperVectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

enum SeExponential implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return MatrixExp.of(v);
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return MatrixLog.of(q);
  }

  @Override // from Exponential
  public TensorUnaryOperator vectorLog() {
    return q -> UpperVectorize.of(log(q), 1);
  }

  @Override // from Exponential
  public ZeroDefectArrayQ isTangentQ() {
    return TSeMemberQ.INSTANCE;
  }
}
