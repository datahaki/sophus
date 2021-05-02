// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.sca.Ramp;

/** owed to the fact that a point of H^n embedded in R^(n+1) corresponds to a
 * vector with (n,1)-norm equals to -1 */
public enum HnPointNorm implements TensorScalarFunction {
  INSTANCE;

  private static final ScalarUnaryOperator OPERATOR = norm -> Ramp.FUNCTION.apply(norm.negate());

  @Override
  public Scalar apply(Tensor x) {
    return HnHypot.of(x, OPERATOR);
  }
}
