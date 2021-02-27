// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BiinvariantVectorFunction;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
