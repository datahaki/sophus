// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Does not work properly with units?
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public class InversePowerVariogram implements ScalarUnaryOperator {
  /** @param alpha
   * @param exponent
   * @return */
  public static InversePowerVariogram of(Scalar alpha, Scalar exponent) {
    return new InversePowerVariogram( //
        Objects.requireNonNull(alpha), //
        Power.function(exponent.negate()));
  }

  /** @param alpha
   * @param exponent
   * @return */
  public static InversePowerVariogram of(Number alpha, Number exponent) {
    return of(RealScalar.of(alpha), RealScalar.of(exponent));
  }

  /***************************************************/
  private final Scalar alpha;
  private final ScalarUnaryOperator power;

  private InversePowerVariogram(Scalar alpha, ScalarUnaryOperator power) {
    this.alpha = alpha;
    this.power = power;
  }

  @Override
  public Scalar apply(Scalar r) {
    return Scalars.isZero(r) //
        ? DoubleScalar.POSITIVE_INFINITY
        : power.apply(r).multiply(alpha);
  }
}
