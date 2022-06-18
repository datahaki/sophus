// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public record BiinvariantVectorCoordinate( //
    BiinvariantVectorFunction biinvariantVectorFunction, ScalarUnaryOperator variogram) //
    implements Sedarim {
  public BiinvariantVectorCoordinate {
    Objects.requireNonNull(biinvariantVectorFunction);
    Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor sunder(Tensor point) {
    return biinvariantVectorFunction.biinvariantVector(point).coordinate(variogram);
  }
}
