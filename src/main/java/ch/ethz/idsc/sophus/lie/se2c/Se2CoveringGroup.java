// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** the covering group of SE(2) is parameterized by R^3 */
public enum Se2CoveringGroup implements LieGroup {
  INSTANCE;
  // ---
  @Override
  public Se2CoveringGroupElement element(Tensor xya) {
    return new Se2CoveringGroupElement(xya);
  }
}
