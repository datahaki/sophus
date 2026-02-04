// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.MultiplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.sca.Chop;

public class PlausibleRational implements ScalarUnaryOperator {
  public static ScalarUnaryOperator of(int i, Chop chop) {
    return new PlausibleRational(i, chop);
  }

  public static ScalarUnaryOperator of(int i) {
    return of(i, Tolerance.CHOP);
  }

  // ---
  private final ScalarUnaryOperator rationalize;
  private final Chop chop;

  private PlausibleRational(int max_den, Chop chop) {
    rationalize = Rationalize.withDenominatorLessEquals(max_den);
    this.chop = chop;
  }

  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof MultiplexScalar multiplexScalar)
      return multiplexScalar.eachMap(this);
    if (ExactScalarQ.of(scalar))
      return scalar;
    if (scalar instanceof RealScalar) {
      Scalar approx = rationalize.apply(scalar);
      if (chop.isClose(approx, scalar))
        return approx;
    }
    return scalar;
  }
}
