// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.api.TensorUnaryOperator;

public enum AppendOne implements TensorUnaryOperator {
  FUNCTION;

  @Override
  public Tensor apply(Tensor tensor) {
    return Append.of(tensor, RealScalar.ONE);
  }
}
