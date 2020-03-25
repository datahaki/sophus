// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;

public interface FlattenLogManifold {
  /** @param point
   * @return operator that maps points on the manifold to a vector in the tangent space at given point */
  FlattenLog logAt(Tensor point);
}
