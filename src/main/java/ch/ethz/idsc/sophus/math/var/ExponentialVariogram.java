// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.util.Objects;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sign;

/** <p>The input of the variogram has unit of a.
 * The output of the variogram has unit of b. */
public class ExponentialVariogram implements ScalarUnaryOperator {
  /** @param a positive
   * @param b
   * @return */
  public static ScalarUnaryOperator of(Scalar a, Scalar b) {
    return new ExponentialVariogram( //
        Sign.requirePositive(a), //
        Objects.requireNonNull(b));
  }

  /** @param a positive
   * @param b
   * @return */
  public static ScalarUnaryOperator of(Number a, Number b) {
    return of(RealScalar.of(a), RealScalar.of(b));
  }

  /***************************************************/
  private final Scalar a;
  private final Scalar b;

  private ExponentialVariogram(Scalar a, Scalar b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public Scalar apply(Scalar r) {
    return RealScalar.ONE.subtract(Exp.FUNCTION.apply(r.divide(a).negate())).multiply(b);
  }
}
