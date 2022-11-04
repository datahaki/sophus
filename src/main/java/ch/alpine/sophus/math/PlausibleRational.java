// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.MultiplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Rationalize;

public enum PlausibleRational implements ScalarUnaryOperator {
  FUNCTION;

  private static final ScalarUnaryOperator RATIONALIZE = //
      Rationalize.withDenominatorLessEquals(10_000);

  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof MultiplexScalar multiplexScalar)
      return multiplexScalar.eachMap(FUNCTION);
    if (ExactScalarQ.of(scalar))
      return scalar;
    if (scalar instanceof RealScalar) {
      Scalar approx = RATIONALIZE.apply(scalar);
      if (Tolerance.CHOP.isClose(approx, scalar))
        return approx;
    }
    return scalar;
  }
}
