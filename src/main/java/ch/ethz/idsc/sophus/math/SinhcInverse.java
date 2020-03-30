// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sinh;

/** Sinhc[z] := Sinh[z] / z */
public enum SinhcInverse implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar z) {
    return Scalars.isZero(z) //
        ? RealScalar.ONE
        : z.divide(Sinh.FUNCTION.apply(z));
  }
}
