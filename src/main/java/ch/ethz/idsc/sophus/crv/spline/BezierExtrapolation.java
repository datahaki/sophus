// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Binomial;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.BinaryAverage;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Power;

/** extrapolation by evaluating the Bezier curve defined by n number of
 * control points at parameter value n / (n - 1) */
public class BezierExtrapolation implements TensorUnaryOperator {
  private static final long serialVersionUID = 4075953813157045900L;

  /** @param binaryAverage
   * @return */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage) {
    return new BezierExtrapolation(binaryAverage);
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;

  private BezierExtrapolation(BinaryAverage binaryAverage) {
    this.binaryAverage = binaryAverage;
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    int n = tensor.length();
    return BezierFunction.of(binaryAverage, tensor).apply(RationalScalar.of(n, n - 1));
  }

  /** The weight mask is generated by the following formula
   * <pre>
   * With[{p = n / (n - 1)}, Table[Binomial[n - 1, k] (1 - p)^(n - k - 1) p^k, {k, 0, n - 1}]]
   * </pre>
   * 
   * The leading coefficient converges to
   * <pre>
   * Limit[(n/(n - 1))^(n - 1), n -> Infinity] == Exp[1]
   * </pre>
   * 
   * @param n
   * @return weight mask of length n with entries that sum up to 1 */
  public static Tensor mask(int n) {
    Scalar p = RationalScalar.of(n, n - 1);
    Scalar o_p = RealScalar.ONE.subtract(p);
    return Tensors.vector(k -> Times.of(Binomial.of(n - 1, k), Power.of(o_p, n - k - 1), Power.of(p, k)), n);
  }
}
