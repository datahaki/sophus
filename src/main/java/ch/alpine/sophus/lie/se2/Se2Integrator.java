// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.lie.LieIntegrator;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;

public enum Se2Integrator implements LieIntegrator {
  INSTANCE;

  /** @param g == {px, py, alpha}
   * @param x == {vx, vy, beta}
   * @return g . exp x */
  @Override // from LieIntegrator
  public Tensor spin(Tensor g, Tensor x) {
    return Se2Group.INSTANCE.element(g).combine(Se2CoveringGroup.INSTANCE.exponential().exp(x));
  }
}
