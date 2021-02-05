// code by jph
package ch.ethz.idsc.sophus.gbc.amp;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Clips;

public enum IdentRamp implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar scalar) {
    return scalar.negate().add(Clips.unit().apply(scalar));
  }
}
