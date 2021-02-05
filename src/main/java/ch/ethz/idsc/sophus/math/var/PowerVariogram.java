// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.io.ScalarArray;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Power;

/** Does not work properly with units?
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public class PowerVariogram implements ScalarUnaryOperator {
  /** Quote:
   * "A good general choice is exponent=3/2, but for functions with a strong linear trend,
   * you may want to experiment with values as large as 1.99."
   * 
   * @param alpha "is fitted by unweighted least squares over all pairs of data points i and j"
   * @param exponent in the range [1, 2) "The value 2 gives a degenerate matrix and meaningless results."
   * @return */
  public static PowerVariogram of(Scalar alpha, Scalar exponent) {
    return new PowerVariogram(Objects.requireNonNull(alpha), Power.function(exponent));
  }

  /** @param alpha
   * @param exponent
   * @return */
  public static PowerVariogram of(Number alpha, Number exponent) {
    return of(RealScalar.of(alpha), RealScalar.of(exponent));
  }

  /** alpha "is fitted by unweighted least squares over all pairs of data points i and j"
   * 
   * @param tensorMetric
   * @param sequence
   * @param values associated to elements of sequence
   * @param exponent in the range [1, 2)
   * @return
   * @throws Exception if values is not a tensor of rank 1 */
  public static PowerVariogram fit(TensorMetric tensorMetric, Tensor sequence, Tensor values, Scalar exponent) {
    Scalar[] y = ScalarArray.ofVector(values);
    final int n = sequence.length();
    Scalar num = y[0].zero();
    // TODO not very elegant generic
    Scalar den = RealScalar.ZERO;
    Scalar nugsq = RealScalar.ZERO;
    ScalarUnaryOperator power = Power.function(exponent);
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        Scalar rb = power.apply(tensorMetric.distance(sequence.get(i), sequence.get(j)));
        num = num.add(AbsSquared.between(y[i], y[j]).multiply(RationalScalar.HALF).subtract(nugsq).multiply(rb));
        den = den.add(rb.multiply(rb));
      }
    return new PowerVariogram(num.divide(den), power);
  }

  /***************************************************/
  private final Scalar alpha;
  private final ScalarUnaryOperator power;

  private PowerVariogram(Scalar alpha, ScalarUnaryOperator power) {
    this.alpha = alpha;
    this.power = power;
  }

  @Override
  public Scalar apply(Scalar r) {
    return power.apply(r).multiply(alpha);
  }

  @Override // from Object
  public final String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), alpha);
  }
}
