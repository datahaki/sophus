// code by ureif
package ch.alpine.sophus.crv.clt;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
public interface Clothoid extends ScalarTensorFunction {
  /** @return non-negative approximate length of clothoid in Euclidean plane */
  Scalar length();

  /** @return */
  Scalar addAngle(Scalar t);

  /** Example:
   * parameter 0 returns curvature of clothoid at p
   * parameter 1 returns curvature of clothoid at q
   * 
   * @return function that evaluates curvature at given parameter */
  LagrangeQuadraticD curvature();
}
