// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Scalar;

/** Intgate [ Exp[ i*Polynomial({c0, c1, c2})[x] ], {x, 0, t} ] */
public interface ClothoidIntegral {
  /** @return at most quadratic polynomial that underlies this integral */
  LagrangeQuadratic lagrangeQuadratic();

  /** interpolation of terminal points
   * t == 0 -> (0, 0)
   * t == 1 -> (1, 0)
   * 
   * @param t
   * @return */
  Scalar normalized(Scalar t);

  /** a return value with Im[one] == 0 implies that {@link #normalized(Scalar)}
   * does not distort tangents at end points t == 0, and t == 1
   * 
   * @return approximate integration of exp i*clothoidQuadratic on [0, 1] */
  Scalar one();
}
