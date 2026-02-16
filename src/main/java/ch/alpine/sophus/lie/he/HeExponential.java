// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

enum HeExponential implements Exponential {
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

  @Override
  public TensorUnaryOperator vectorLog() {
    return log();
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.INSTANCE;
  }
}
