// code by jph
package ch.alpine.sophus.math.win;

import java.io.Serializable;
import java.util.function.Function;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;

/* package */ abstract class BaseWindowSampler implements Function<Integer, Tensor>, Serializable {
  private static final Tensor SINGLETON = Tensors.vector(1).unmodifiable();
  // ---
  protected final ScalarUnaryOperator windowFunction;
  protected final boolean isContinuous;

  /** @param windowFunction for evaluation in the interval [-1/2, +1/2] */
  protected BaseWindowSampler(ScalarUnaryOperator windowFunction) {
    this.windowFunction = windowFunction;
    isContinuous = Chop._03.isZero(windowFunction.apply(RationalScalar.HALF));
  }

  @Override // from IntegerTensorFunction
  public final Tensor apply(Integer length) {
    return 1 == Integers.requirePositive(length) //
        ? SINGLETON
        : NormalizeTotal.FUNCTION.apply(samples(length)).unmodifiable();
  }

  /** @param length 2 or greater
   * @return vector of weights of given length, not normalized */
  protected abstract Tensor samples(int length);
}
