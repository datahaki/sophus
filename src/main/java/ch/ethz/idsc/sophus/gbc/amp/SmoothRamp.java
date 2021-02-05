// code by jph
package ch.ethz.idsc.sophus.gbc.amp;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Sign;

public class SmoothRamp implements ScalarUnaryOperator {
  private final Scalar sigma;

  public SmoothRamp(Scalar sigma) {
    this.sigma = Sign.requirePositive(sigma);
  }

  @Override
  public Scalar apply(Scalar scalar) {
    Scalar product = scalar.multiply(sigma);
    return Sign.isPositiveOrZero(scalar) //
        ? RealScalar.ONE.add(product)
        : Exp.FUNCTION.apply(product);
  }
}
