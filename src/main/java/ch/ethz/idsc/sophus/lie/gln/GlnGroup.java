// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** Lie group GL(n) of invertible square matrices
 * also called "immersely linear Lie group" */
public enum GlnGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public GlnGroupElement element(Tensor matrix) {
    return GlnGroupElement.of(matrix);
  }
}
