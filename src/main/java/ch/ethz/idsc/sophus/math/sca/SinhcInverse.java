// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sinh;

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
