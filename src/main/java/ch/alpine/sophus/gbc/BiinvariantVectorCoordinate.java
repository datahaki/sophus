// code by jph
package ch.alpine.sophus.gbc;

import java.util.Objects;

import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class BiinvariantVectorCoordinate implements TensorUnaryOperator {
  private final BiinvariantVectorFunction biinvariantVectorFunction;
  private final ScalarUnaryOperator variogram;

  public BiinvariantVectorCoordinate(BiinvariantVectorFunction biinvariantVectorFunction, ScalarUnaryOperator variogram) {
    this.biinvariantVectorFunction = Objects.requireNonNull(biinvariantVectorFunction);
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor apply(Tensor point) {
    return biinvariantVectorFunction.biinvariantVector(point).coordinate(variogram);
  }
}
