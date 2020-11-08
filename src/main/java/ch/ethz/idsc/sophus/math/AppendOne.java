// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

public enum AppendOne implements TensorUnaryOperator {
  FUNCTION;

  @Override
  public Tensor apply(Tensor tensor) {
    return Append.of(tensor, RealScalar.ONE);
  }
}
