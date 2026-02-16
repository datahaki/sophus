// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.sophus.lie.sl.TSlMemberQ;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

/* package */ enum PGlExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return PGlGroup.protected_project(MatrixExp.of(matrix));
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSlMemberQ.INSTANCE; // sl according to chatgpt
  }
}
