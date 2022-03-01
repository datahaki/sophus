// code by jph
package ch.alpine.sophus.math.var;

import java.util.Objects;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.ScalarArray;
import ch.alpine.tensor.red.LenientAdd;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.Power;

/** Does not work properly with units?
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public record PowerVariogram(Scalar alpha, ScalarUnaryOperator power) implements ScalarUnaryOperator {
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
  // TODO fit function needs a demo
  @SuppressWarnings("null")
  public static PowerVariogram fit(TensorMetric tensorMetric, Tensor sequence, Tensor values, Scalar exponent) {
    Scalar[] y = ScalarArray.ofVector(values);
    final int n = sequence.length();
    Scalar num = null; // 0[|seq_0| * v[0] * v[0]]
    // TODO not very elegant generic
    Scalar den = RealScalar.ZERO;
    Scalar nugsq = RealScalar.ZERO;
    ScalarUnaryOperator power = Power.function(exponent);
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        Scalar rb = power.apply(tensorMetric.distance(sequence.get(i), sequence.get(j)));
        Scalar val = LenientAdd.of( //
            AbsSquared.between(y[i], y[j]).multiply(RationalScalar.HALF), nugsq.negate()).multiply(rb);
        num = Objects.isNull(num) ? val : num.add(val);
        den = LenientAdd.of( //
            den, rb.multiply(rb));
      }
    return new PowerVariogram(num.divide(den), power);
  }

  @Override
  public Scalar apply(Scalar r) {
    return power.apply(r).multiply(alpha);
  }
}
