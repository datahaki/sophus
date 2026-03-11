// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

public enum SlExponential implements LieExponential {
  INSTANCE;

  @Override
  public Tensor exp(Tensor v) {
    return MatrixExp.of(v);
  }

  @Override
  public Tensor log(Tensor q) {
    return MatrixLog.of(q);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSlMemberQ.INSTANCE;
  }
}
