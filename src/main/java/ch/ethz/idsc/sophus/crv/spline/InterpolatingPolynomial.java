// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/InterpolatingPolynomial.html">InterpolatingPolynomial</a> */
public class InterpolatingPolynomial implements ScalarUnaryOperator {
  /** @param knots vector
   * @param values vector
   * @return */
  public static ScalarUnaryOperator of(Tensor knots, Tensor values) {
    return new InterpolatingPolynomial(knots, VectorQ.requireLength(values, knots.length()));
  }

  /***************************************************/
  private final ScalarTensorFunction scalarTensorFunction;

  private InterpolatingPolynomial(Tensor knots, Tensor values) {
    scalarTensorFunction = new GeodesicNeville(RnGeodesic.INSTANCE, knots, values);
  }

  @Override
  public Scalar apply(Scalar t) {
    return (Scalar) scalarTensorFunction.apply(t);
  }
}
