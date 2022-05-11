// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface Manifold {
  /** @param p
   * @return exponential map at given point p on manifold */
  Exponential exponential(Tensor p);
}
