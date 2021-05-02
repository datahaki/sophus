// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Sinh;

/** Sinhc[z] := Sinh[z] / z */
public enum Sinhc implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar z) {
    return Scalars.isZero(z) //
        ? RealScalar.ONE
        : Sinh.FUNCTION.apply(z).divide(z);
  }
}
