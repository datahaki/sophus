// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.tensor.Tensor;

/** Lie group GL(n) of invertible square matrices
 * also called "immersely linear Lie group" */
public enum GlGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public GlGroupElement element(Tensor matrix) {
    return GlGroupElement.of(matrix);
  }
}
