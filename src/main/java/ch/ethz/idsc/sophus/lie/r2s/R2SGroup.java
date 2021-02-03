// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.he.HeGroup;
import ch.ethz.idsc.tensor.Tensor;

/** @see HeGroup */
public enum R2SGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public LieGroupElement element(Tensor xyu) {
    return new R2SGroupElement(xyu);
  }
}
