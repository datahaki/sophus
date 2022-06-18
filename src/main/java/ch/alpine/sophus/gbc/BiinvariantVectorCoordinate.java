// code by jph
package ch.alpine.sophus.gbc;

import java.util.Objects;

import ch.alpine.sophus.dv.BiinvariantVectorFunction;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

public record BiinvariantVectorCoordinate( //
    BiinvariantVectorFunction biinvariantVectorFunction, ScalarUnaryOperator variogram) //
    implements TensorUnaryOperator {
  public BiinvariantVectorCoordinate {
    Objects.requireNonNull(biinvariantVectorFunction);
    Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor apply(Tensor point) {
    return biinvariantVectorFunction.biinvariantVector(point).coordinate(variogram);
  }
}
