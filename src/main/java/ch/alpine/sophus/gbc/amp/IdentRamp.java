// code by jph
package ch.alpine.sophus.gbc.amp;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Clips;

public enum IdentRamp implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar scalar) {
    return scalar.negate().add(Clips.unit().apply(scalar));
  }
}
