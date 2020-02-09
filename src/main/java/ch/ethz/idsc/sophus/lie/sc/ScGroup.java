// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

public enum ScGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public ScGroupElement element(Tensor tensor) {
    return ScGroupElement.of(tensor);
  }
}
