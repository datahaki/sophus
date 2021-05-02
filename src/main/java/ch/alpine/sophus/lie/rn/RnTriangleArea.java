// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Sqrt;

/** https://en.wikipedia.org/wiki/Heron%27s_formula */
public enum RnTriangleArea {
  ;
  /** @param p0
   * @param p1
   * @param p2
   * @return */
  public static Scalar of(Tensor p0, Tensor p1, Tensor p2) {
    Scalar a = Vector2Norm.between(p0, p1);
    Scalar b = Vector2Norm.between(p1, p2);
    Scalar c = Vector2Norm.between(p2, p0);
    Scalar s = a.add(b).add(c).multiply(RationalScalar.HALF);
    return Sqrt.FUNCTION.apply(Times.of(s, s.subtract(a), s.subtract(b), s.subtract(c)));
  }
}
