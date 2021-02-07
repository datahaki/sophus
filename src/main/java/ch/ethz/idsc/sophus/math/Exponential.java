// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.tensor.Tensor;

/** map between manifold M and tangent space T_x M for some point x in M
 * 
 * Log[g.m.g^-1] == Ad[g].Log[m] */
public interface Exponential extends TangentSpace {
  /** @param vector in the tangent space
   * @return point in the manifold */
  Tensor exp(Tensor vector);

  /** @param point in the manifold
   * @return vector in the tangent space */
  Tensor log(Tensor point);
}
