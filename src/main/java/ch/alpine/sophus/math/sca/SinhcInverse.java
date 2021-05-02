// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Sinh;

/** SinhcInverse[z] := z / Sinh[z] */
public enum SinhcInverse implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar z) {
    return Scalars.isZero(z) //
        ? RealScalar.ONE
        : z.divide(Sinh.FUNCTION.apply(z));
  }
}
