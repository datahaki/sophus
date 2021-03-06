// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Power;

/** Does not work properly with units?
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public class InversePowerVariogram implements ScalarUnaryOperator {
  /** @param exponent for instance 2
   * @return */
  public static ScalarUnaryOperator of(Scalar exponent) {
    if (exponent.equals(RealScalar.ZERO))
      return scalar -> RealScalar.ONE;
    return new InversePowerVariogram(exponent.equals(RealScalar.ONE) //
        ? Scalar::reciprocal
        : Power.function(exponent.negate()));
  }

  /** @param exponent for instance 2
   * @return */
  public static ScalarUnaryOperator of(Number exponent) {
    return of(RealScalar.of(exponent));
  }

  /***************************************************/
  private final ScalarUnaryOperator power;

  private InversePowerVariogram(ScalarUnaryOperator power) {
    this.power = power;
  }

  @Override
  public Scalar apply(Scalar r) {
    return Scalars.isZero(r) //
        ? DoubleScalar.POSITIVE_INFINITY
        : power.apply(r);
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), power);
  }
}
