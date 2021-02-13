// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

/* package */ enum InverseNorm implements Genesis {
  INSTANCE;

  private static final ScalarUnaryOperator VARIOGRAM = InversePowerVariogram.of(1);

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream() //
        .map(VectorNorm2::of) //
        .map(VARIOGRAM));
  }
}
