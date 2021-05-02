// code by jph
package ch.alpine.sophus.crv.se2c;

import java.util.Objects;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.sca.ArcTan;
import ch.alpine.tensor.sca.Exp;

/** https://en.wikipedia.org/wiki/Logarithmic_spiral */
public class LogarithmicSpiral implements ScalarTensorFunction {
  /** @param a
   * @param b for instance 0.1759 */
  public static ScalarTensorFunction of(Scalar a, Scalar b) {
    return new LogarithmicSpiral( //
        Objects.requireNonNull(a), //
        Objects.requireNonNull(b));
  }

  /** @param a
   * @param b
   * @return */
  public static ScalarTensorFunction of(Number a, Number b) {
    return new LogarithmicSpiral( //
        RealScalar.of(a), //
        RealScalar.of(b));
  }

  /***************************************************/
  private final Scalar a;
  private final Scalar b;

  private LogarithmicSpiral(Scalar a, Scalar b) {
    this.a = a;
    this.b = b;
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
