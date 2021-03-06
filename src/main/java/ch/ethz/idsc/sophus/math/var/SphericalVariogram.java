// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.util.Objects;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.num.Series;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * Eq (15.9.6) "Gaussian Process Regression" in NR, 2007
 * 
 * <p>The input of the variogram has unit of a.
 * The output of the variogram has unit of b. */
public class SphericalVariogram implements ScalarUnaryOperator {
  /** @param a positive
   * @param b
   * @return */
  public static ScalarUnaryOperator of(Scalar a, Scalar b) {
    return new SphericalVariogram( //
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
  private final ScalarUnaryOperator series;

  private SphericalVariogram(Scalar a, Scalar b) {
    this.a = a;
    this.b = b;
    series = Series.of(Tensors.of( //
        RealScalar.ZERO, //
        RationalScalar.of(+3, 2), //
        RealScalar.ZERO, //
        RationalScalar.of(-1, 2)).multiply(b));
  }

  @Override
  public Scalar apply(Scalar r) {
    return Scalars.lessThan(r, a) //
        ? series.apply(r.divide(a))
        : b;
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s, %s]", getClass().getSimpleName(), a, b);
  }
}
