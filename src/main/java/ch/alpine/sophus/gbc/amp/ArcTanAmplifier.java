// code by jph
package ch.alpine.sophus.gbc.amp;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.ArcTan;
import ch.alpine.tensor.sca.Sign;

/**
 * 
 */
public class ArcTanAmplifier implements ScalarUnaryOperator {
  private final Scalar sigma;

  public ArcTanAmplifier(Scalar sigma) {
    this.sigma = Sign.requirePositive(sigma);
  }

  @Override
  public Scalar apply(Scalar scalar) {
    Scalar value = ArcTan.FUNCTION.apply(scalar.multiply(sigma));
    return value.add(value).divide(Pi.VALUE).add(RealScalar.ONE);
  }
}
