// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface BiinvariantVectorFunction {
  /** @param point
   * @return biinvariant vector at given point of manifold */
  BiinvariantVector biinvariantVector(Tensor point);
}
