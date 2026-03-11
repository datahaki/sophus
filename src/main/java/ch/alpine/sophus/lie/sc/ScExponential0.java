// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Log;

enum ScExponential0 implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    return VectorQ.require(x).maps(Exp.FUNCTION);
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    return VectorQ.require(g).maps(Log.FUNCTION);
  }

  @Override // from Exponential
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.INSTANCE;
  }

  @Override // from Exponential
  public Tensor gl_representation(Tensor x) {
    return DiagonalMatrix.full(x);
  }
}
