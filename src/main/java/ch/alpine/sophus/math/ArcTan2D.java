// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.ArcTan;

/** Hint: ArcTan2D[{0, 0}] == 0.0
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/ArcTan.html">ArcTan</a> */
public enum ArcTan2D {
  ;
  /** @param vector of the form {x, y, ...}
   * @return ArcTan[x, y] */
  public static Scalar of(Tensor vector) {
    return ArcTan.of(vector.Get(0), vector.Get(1));
  }
}
