// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

public enum Sl2Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Sl2GroupElement element(Tensor vector) {
    return Sl2GroupElement.create(vector);
  }

  @Override
  public Exponential exponential() {
    return Sl2Exponential.INSTANCE;
  }
}
