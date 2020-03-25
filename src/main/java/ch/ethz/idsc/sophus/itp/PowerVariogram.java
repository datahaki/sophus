// code by jph
package ch.ethz.idsc.sophus.itp;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Does not work properly with units?
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public class PowerVariogram implements ScalarUnaryOperator {
  /** Quote:
   * "A good general choice is beta=3/2, but for functions with a strong linear trend,
   * you may want to experiment with values as large as 1.99."
   * 
   * @param alpha "is fitted by unweighted least squares over all pairs of data points i and j"
   * @param beta in the range [1, 2) "The value 2 gives a degenerate matrix and meaningless results."
   * @return */
  public static PowerVariogram of(Scalar alpha, Scalar beta) {
    if (beta.equals(RealScalar.of(2)))
      throw TensorRuntimeException.of(beta);
    return new PowerVariogram(Objects.requireNonNull(alpha), beta);
  }

  /** @param alpha
   * @param beta
   * @return */
  public static PowerVariogram of(Number alpha, Number beta) {
    return of(RealScalar.of(alpha), RealScalar.of(beta));
  }

  /** alpha "is fitted by unweighted least squares over all pairs of data points i and j"
   * 
   * @param sequence
   * @param values vector
   * @param beta
   * @return
   * @throws Exception if values is not a tensor of rank 1 */
  public static PowerVariogram fit(Tensor sequence, Tensor values, Scalar beta) {
    Scalar[] y = values.stream().map(Scalar.class::cast).toArray(Scalar[]::new);
    final int n = sequence.length();
    Scalar num = RealScalar.ZERO;
    Scalar denom = RealScalar.ZERO;
    Scalar nugsq = RealScalar.ZERO;
    ScalarUnaryOperator power = Power.function(beta);
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        Scalar rb = power.apply(RnMetric.INSTANCE.distance(sequence.get(i), sequence.get(j)));
        num = num.add(AbsSquared.FUNCTION.apply(y[i].subtract(y[j])).multiply(RationalScalar.HALF).subtract(nugsq).multiply(rb));
        denom = denom.add(rb.multiply(rb));
      }
    return of(num.divide(denom), beta);
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

  public Scalar alpha() {
    return alpha;
  }
}
