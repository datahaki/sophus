// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.Vector2Norm;

/* package */ enum InverseNorm implements Genesis {
  INSTANCE;

  private static final ScalarUnaryOperator VARIOGRAM = InversePowerVariogram.of(1);

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream() //
        .map(Vector2Norm::of) //
        .map(VARIOGRAM));
  }
}
