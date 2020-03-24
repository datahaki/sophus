// code by jph
package ch.ethz.idsc.sophus.itp;

import java.util.Objects;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public class PowerVariogram implements ScalarUnaryOperator {
  /** Quote:
   * "A good general choice is 1.5, but for functions with a strong linear trend,
   * you may want to experiment with values as large as 1.99."
   * 
   * @param alpha "is fitted by unweighted least squares over all pairs of data points i and j"
   * @param beta in the range [1, 2) "The value 2 gives a degenerate matrix and meaningless results."
   * @return */
  public static ScalarUnaryOperator of(Scalar alpha, Scalar beta) {
    if (beta.equals(RealScalar.of(2)))
      throw TensorRuntimeException.of(beta);
    return new PowerVariogram(Objects.requireNonNull(alpha), beta);
  }

  /** @param alpha
   * @param beta
   * @return */
  public static ScalarUnaryOperator of(Number alpha, Number beta) {
    return of(RealScalar.of(alpha), RealScalar.of(beta));
  }

  /***************************************************/
  private final Scalar alpha;
  private final ScalarUnaryOperator power;

  private PowerVariogram(Scalar alpha, Scalar beta) {
    this.alpha = alpha;
    power = Power.function(beta);
  }

  @Override
  public Scalar apply(Scalar r) {
    return power.apply(r).multiply(alpha);
  }
}
