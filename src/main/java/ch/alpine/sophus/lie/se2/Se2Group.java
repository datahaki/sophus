// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.tensor.Tensor;

/** parameterized by R^2 x [-pi, pi) */
public enum Se2Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Se2GroupElement element(Tensor xya) {
    return new Se2GroupElement(xya);
  }

  @Override
  public Exponential exponential() {
    return Se2CoveringExponential.INSTANCE;
  }
}
