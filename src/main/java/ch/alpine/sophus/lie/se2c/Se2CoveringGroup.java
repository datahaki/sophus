// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

/** the covering group of SE(2) is parameterized by R^3 */
public enum Se2CoveringGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Se2CoveringGroupElement element(Tensor xya) {
    return new Se2CoveringGroupElement(xya);
  }

  @Override
  public Exponential exponential() {
    return Se2CoveringExponential.INSTANCE;
  }
}
