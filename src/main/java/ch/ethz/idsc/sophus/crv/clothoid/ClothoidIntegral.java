// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;

/** The integral of exp i*clothoidQuadratic as suggested in U. Reif slides has the
 * property, that the values of the polynomial correspond to the tangent angle. */
public interface ClothoidIntegral {
  /** interpolation of terminal points
   * t == 0 -> (0, 0)
   * t == 1 -> (1, 0) */
  Scalar normalized(Scalar t);

  /** @param t
   * @return approximate integration of exp i*clothoidQuadratic on [0, t] */
  Scalar il(Scalar t);

  /** @return approximate integration of exp i*clothoidQuadratic on [0, 1] */
  Scalar one();
}
