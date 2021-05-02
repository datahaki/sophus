// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface VectorLogManifold {
  /** @param point on manifold
   * @return operator that maps points on the manifold to a vector in the tangent space at given point */
  TangentSpace logAt(Tensor point);
}
