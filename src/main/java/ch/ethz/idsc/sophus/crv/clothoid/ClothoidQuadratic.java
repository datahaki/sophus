// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;

/** Equations:
 * s1 == (b0 + b1) / 2
 * s2 == (b0 - b1) / 2
 * 
 * b0 == s1 + s2
 * b1 == s1 - s2 */
@FunctionalInterface
public interface ClothoidQuadratic {
  /** @param b0 angle
   * @param b1 angle
   * @return */
  LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1);
}
