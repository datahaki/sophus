// code by jph
package ch.alpine.sophus.gbc.amp;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Exp;
import ch.alpine.tensor.sca.Sign;

public record SmoothRamp(Scalar sigma) implements ScalarUnaryOperator {
  public SmoothRamp {
    Sign.requirePositive(sigma);
  }

  @Override
  public Scalar apply(Scalar scalar) {
    Scalar product = scalar.multiply(sigma);
    return Sign.isPositiveOrZero(scalar) //
        ? RealScalar.ONE.add(product)
        : Exp.FUNCTION.apply(product);
  }
}
