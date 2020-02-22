// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1 */
public enum So3Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public So3GroupElement element(Tensor matrix) {
    return So3GroupElement.of(matrix);
  }
}
