// code by jph
package ch.ethz.idsc.sophus.gbc.amp;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.Sign;

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
