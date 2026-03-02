// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

enum RExponential0 implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return v.copy();
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return y.copy();
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.INSTANCE;
  }

  @Override
  public Tensor gl_representation(Tensor x) {
    int n = x.length();
    Tensor zeros = Array.zeros(n);
    Tensor matrix = Tensor.of(x.stream().map(r -> Append.of(zeros, r)));
    return matrix.append(Array.zeros(n + 1));
  }
}
