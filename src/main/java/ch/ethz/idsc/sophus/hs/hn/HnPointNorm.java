// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.sca.Ramp;

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
