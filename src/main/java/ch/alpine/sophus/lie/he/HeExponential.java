// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

enum HeExponential implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor uvw) {
    return HeFormat.of(uvw).exp().toCoordinate();
  }

  /** @param xyz vector of the form {x1, ...., xn, y1, ..., yn, z} */
  @Override // from Exponential
  public Tensor log(Tensor xyz) {
    return HeFormat.of(xyz).log().toCoordinate();
  }

  @Override // from Exponential
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.INSTANCE;
  }

  @Override // from LieExponential
  public Tensor gl_representation(Tensor uvw) {
    HeFormat heFormat = HeFormat.of(uvw);
    int n = heFormat.x().length();
    int last = n + 1;
    Tensor matrix = Array.zeros(n + 2, n + 2);
    for (int i = 0; i < n; ++i) {
      matrix.set(heFormat.x().get(i), 0, 1 + i);
      matrix.set(heFormat.y().get(i), 1 + i, last);
    }
    matrix.set(heFormat.z(), 0, last);
    return matrix;
  }
}
