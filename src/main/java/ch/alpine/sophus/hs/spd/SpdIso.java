package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.MatrixDotTranspose;

record SpdIso(Tensor g) implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor p) {
    return MatrixDotTranspose.of(g.dot(p), g);
  }
}
