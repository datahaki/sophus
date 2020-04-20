// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public enum Se2CoveringTransport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    // Se2CoveringGroupElement gd = Se2CoveringGroup.INSTANCE.element(dest);
    // Tensor lg = gd.combine(Se2CoveringGroup.INSTANCE.element(orig).inverse().toCoordinate());
    // return Se2CoveringGroup.INSTANCE.element(lg)::dL;
    return t -> t;
  }
}
