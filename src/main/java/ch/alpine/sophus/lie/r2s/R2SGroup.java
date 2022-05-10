// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.tensor.Tensor;

/** @see HeGroup */
public enum R2SGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public LieGroupElement element(Tensor xyu) {
    return new R2SGroupElement(xyu);
  }

  @Override
  public Exponential exponential() {
    return R2SExponential.INSTANCE;
  }
}
