// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1 */
public enum SoGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public SoGroupElement element(Tensor matrix) {
    return SoGroupElement.of(matrix);
  }
}
