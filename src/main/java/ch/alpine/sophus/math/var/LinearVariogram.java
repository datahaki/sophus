// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Sign;

public record LinearVariogram(Scalar a) implements ScalarUnaryOperator {
  public LinearVariogram {
    Sign.requirePositive(a);
  }

  @Override
  public Scalar apply(Scalar t) {
    return t.multiply(a);
  }
}
