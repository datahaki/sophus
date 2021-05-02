// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

/** (2*n+1)-dimensional Heisenberg group */
public enum HeGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public HeGroupElement element(Tensor xyz) {
    return new HeGroupElement(xyz);
  }
}
