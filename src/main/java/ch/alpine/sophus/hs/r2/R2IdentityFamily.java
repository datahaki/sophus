// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;

public enum R2IdentityFamily implements R2RigidFamily {
  INSTANCE;

  private static final Tensor MATRIX = IdentityMatrix.of(3).unmodifiable();

  @Override
  public TensorUnaryOperator inverse(Scalar scalar) {
    return t -> t;
  }

  @Override
  public TensorUnaryOperator forward(Scalar scalar) {
    return t -> t;
  }

  @Override
  public Tensor forward_se2(Scalar scalar) {
    return MATRIX;
  }
}
