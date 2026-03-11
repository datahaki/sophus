// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

enum RnExponential implements LieExponential {
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
    // TODO this is does not corresponds to RGroup which allows scalar, and matrices
    return VectorQ.INSTANCE;
  }

  @Override // from LieExponential
  public Tensor gl_representation(Tensor _x) {
    Tensor x = Flatten.of(_x); // <- allows all tensors instead of only vectors
    int n = x.length();
    Tensor zeros = Array.zeros(n);
    Tensor matrix = Tensor.of(x.stream().map(r -> Append.of(zeros, r)));
    return matrix.append(Array.zeros(n + 1));
  }
}
