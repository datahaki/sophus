// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

public enum AveragingWeights implements Genesis {
  INSTANCE;

  public static Tensor of(int n) {
    return ConstantArray.of(RationalScalar.of(1, n), n);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return of(levers.length());
  }
}
