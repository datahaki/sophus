// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/* package */ record BiinvariantVectorCoordinate( //
    BiinvariantVectorFunction biinvariantVectorFunction, ScalarUnaryOperator variogram) //
    implements Sedarim {
  @Override // from Sedarim
  public Tensor sunder(Tensor point) {
    return biinvariantVectorFunction.biinvariantVector(point).coordinate(variogram);
  }
}
