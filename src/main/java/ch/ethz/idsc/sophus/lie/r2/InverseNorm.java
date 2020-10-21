// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

/* package */ enum InverseNorm implements Genesis {
  INSTANCE;

  private static final ScalarUnaryOperator VARIOGRAM = InversePowerVariogram.of(1);

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream() //
        .map(Norm._2::ofVector) //
        .map(VARIOGRAM));
  }
}
