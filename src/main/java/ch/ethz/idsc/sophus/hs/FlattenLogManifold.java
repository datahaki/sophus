// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface FlattenLogManifold {
  /** @param point
   * @return operator that maps points on the manifold to a vector in the tangent space at given point */
  FlattenLog logAt(Tensor point); // TODO should provide exp at point as well
}
