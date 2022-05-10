// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.so3.So3Group;
import ch.alpine.tensor.Tensor;

/** special orthogonal group of n x n orthogonal matrices with determinant 1
 * 
 * for n == 3 use {@link So3Group} */
public enum SoGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public SoGroupElement element(Tensor matrix) {
    return SoGroupElement.of(matrix);
  }

  @Override
  public Exponential exponential() {
    return SoExponential.INSTANCE;
  }
}
