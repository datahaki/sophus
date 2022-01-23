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
public record ArcTanAmplifier(Scalar sigma) implements ScalarUnaryOperator {
  public ArcTanAmplifier {
    Sign.requirePositive(sigma);
  }

  @Override
  public Scalar apply(Scalar scalar) {
    return ArcTan.FUNCTION.apply(scalar.multiply(sigma)).divide(Pi.HALF).add(RealScalar.ONE);
  }
}
