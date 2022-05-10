// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

public enum ScGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public ScGroupElement element(Tensor tensor) {
    return ScGroupElement.of(tensor);
  }

  @Override
  public Exponential exponential() {
    return ScExponential.INSTANCE;
  }
}
