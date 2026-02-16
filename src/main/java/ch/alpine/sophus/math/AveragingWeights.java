// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

public enum AveragingWeights {
  ;
  public static Tensor of(int n) {
    return ConstantArray.of(Rational.of(1, n), n);
  }
}
