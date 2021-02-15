// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** interface that extends the capabilities of vector log manifold
 * hs exponential provides the exp function to move from a point p
 * to a point q on the manifold, via the tangent vector at p */
public interface HsManifold extends VectorLogManifold {
  /** @param point
   * @return exponential map at given point on manifold */
  Exponential exponential(Tensor point);
}
