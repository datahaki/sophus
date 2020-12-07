// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** https://en.wikipedia.org/wiki/Heron%27s_formula */
public enum RnTriangleArea {
  ;
  /** @param p0
   * @param p1
   * @param p2
   * @return */
  public static Scalar of(Tensor p0, Tensor p1, Tensor p2) {
    Scalar a = Norm._2.between(p0, p1);
    Scalar b = Norm._2.between(p1, p2);
    Scalar c = Norm._2.between(p2, p0);
    Scalar s = a.add(b).add(c).multiply(RationalScalar.HALF);
    return Sqrt.FUNCTION.apply(Times.of(s, s.subtract(a), s.subtract(b), s.subtract(c)));
  }
}
