// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface LieIntegrator {
  /** @param g element of the Lie-group
   * @param x element of the Lie-algebra
   * @return g . exp[x] */
  Tensor spin(Tensor g, Tensor x);
}
