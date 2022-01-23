// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Sinh;

/** SinhcInverse[z] := z / Sinh[z] */
public enum SinhcInverse implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar z) {
    Scalar value = Sinh.FUNCTION.apply(z);
    return Scalars.isZero(z) //
        ? z.one()
        : z.divide(value);
  }
}
