// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

public enum AveragingWeights implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    int n = levers.length();
    return ConstantArray.of(Rational.of(1, n), n);
  }
}
