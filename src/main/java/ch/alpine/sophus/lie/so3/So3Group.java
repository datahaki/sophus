// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.tensor.Tensor;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1 */
public enum So3Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public SoGroupElement element(Tensor matrix) {
    return SoGroupElement.of(matrix);
  }

  @Override
  public Exponential exponential() {
    return So3Exponential.INSTANCE;
  }
}
