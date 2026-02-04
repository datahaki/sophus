// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.tri.ArcTan;

/** https://en.wikipedia.org/wiki/Logarithmic_spiral
 * 
 * @param a
 * @param b for instance 0.1759 */
public record LogarithmicSpiral(Scalar a, Scalar b) implements ScalarTensorFunction {
  /** @param a
   * @param b
   * @return */
  public static ScalarTensorFunction of(Number a, Number b) {
    return new LogarithmicSpiral( //
        RealScalar.of(a), //
        RealScalar.of(b));
  }

  @Override // from ScalarTensorFunction
  public Tensor apply(Scalar theta) {
    Scalar radius = a.multiply(Exp.FUNCTION.apply(b.multiply(theta)));
    Tensor cossin = AngleVector.of(theta);
    Scalar tangen = ArcTan.of( //
        cossin.Get(0).multiply(b).subtract(cossin.Get(1)), //
        cossin.Get(1).multiply(b).add(cossin.Get(0))); // not scaled according to parameterization!
    return cossin.multiply(radius).append(tangen);
  }
}
