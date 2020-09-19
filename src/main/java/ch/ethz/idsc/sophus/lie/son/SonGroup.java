// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1 */
public enum SonGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public SonGroupElement element(Tensor matrix) {
    return SonGroupElement.of(matrix);
  }
}
