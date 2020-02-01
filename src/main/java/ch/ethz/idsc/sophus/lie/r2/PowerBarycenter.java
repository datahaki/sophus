// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.function.BiFunction;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class PowerBarycenter implements BiFunction<Tensor, Scalar, Tensor>, Serializable {
  /** @param exponent in the interval [0, 2] */
  public static BiFunction<Tensor, Scalar, Tensor> of(Scalar exponent) {
    return new PowerBarycenter(exponent);
  }

  public static BiFunction<Tensor, Scalar, Tensor> of(Number exponent) {
    return of(RealScalar.of(exponent));
  }

  // ---
  private final ScalarUnaryOperator power;

  private PowerBarycenter(Scalar exponent) {
    power = Power.function(exponent);
  }

  @Override
  public Tensor apply(Tensor dif, Scalar nrm) {
    return dif.divide(power.apply(nrm));
  }
}
