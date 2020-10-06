// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;

/* package */ enum AffineGenesis implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return ConstantArray.of(RealScalar.ONE, levers.length());
  }
}
