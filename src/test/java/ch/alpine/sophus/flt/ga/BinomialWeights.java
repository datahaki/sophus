// code by jph
package ch.alpine.sophus.flt.ga;

import java.util.function.Function;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.num.Binomial;
import ch.alpine.tensor.sca.Power;

public enum BinomialWeights implements Function<Integer, Tensor> {
  INSTANCE;

  @Override
  public Tensor apply(Integer i) {
    Integers.requirePositive(i);
    return Tensors.vector(k -> Binomial.of(i - 1, k), i).divide(Power.of(2, i - 1));
  }
}
