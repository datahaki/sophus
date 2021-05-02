// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;

/** Lie group GL(n) of invertible square matrices
 * also called "immersely linear Lie group" */
public enum GlGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public GlGroupElement element(Tensor matrix) {
    return GlGroupElement.of(matrix);
  }
}
