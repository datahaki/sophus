// code by ob, jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.exp.Log;

public enum Decibel implements ScalarUnaryOperator {
  FUNCTION;

  private static final ScalarUnaryOperator LOG10 = Log.base(10);
  private static final Scalar _20 = RealScalar.of(20);

  @Override
  public Scalar apply(Scalar scalar) {
    return LOG10.apply(scalar).multiply(_20);
  }
}
