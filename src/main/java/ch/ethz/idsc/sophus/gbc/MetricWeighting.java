// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.krg.MetricDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** @see MetricDistances */
/* package */ class MetricWeighting implements Genesis, Serializable {
  private final ScalarUnaryOperator variogram;

  public MetricWeighting(ScalarUnaryOperator variogram) {
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(Norm._2::ofVector) //
        .map(variogram)));
  }
}
