// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

public enum So3Group implements LieGroup {
  INSTANCE;

  @Override
  public So3GroupElement element(Tensor matrix) {
    return So3GroupElement.of(matrix);
  }
}
