// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.qty.Quantity;

// TODO SOPHUS API highly experimental
public enum PlausibleRational implements ScalarUnaryOperator {
  FUNCTION;

  private static final ScalarUnaryOperator RATIONALIZE = //
      Rationalize.withDenominatorLessEquals(10_000);

  @Override
  public Scalar apply(Scalar t) {
    if (ExactScalarQ.of(t))
      return t;
    if (t instanceof Quantity q)
      return Quantity.of(q.value(), q.unit());
    if (t instanceof ComplexScalar c)
      return ComplexScalar.of( //
          apply(c.real()), //
          apply(c.imag()));
    if (t instanceof RealScalar) {
      if (Scalars.isZero(t))
        return RealScalar.ZERO;
      Scalar approx = RATIONALIZE.apply(t);
      if (Tolerance.CHOP.isClose(approx, t))
        return approx;
    }
    return t;
  }
}
