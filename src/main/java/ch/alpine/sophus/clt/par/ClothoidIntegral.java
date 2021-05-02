// code by jph
package ch.alpine.sophus.clt.par;

import ch.alpine.tensor.Scalar;

public interface ClothoidIntegral {
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
