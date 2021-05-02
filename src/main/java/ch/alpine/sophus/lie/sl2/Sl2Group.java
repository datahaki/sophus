// code by jph
package ch.alpine.sophus.lie.sl2;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

public enum Sl2Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Sl2GroupElement element(Tensor vector) {
    return Sl2GroupElement.create(vector);
  }
}