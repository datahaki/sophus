// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.function.Function;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ abstract class BaseWindowSampler implements Function<Integer, Tensor>, Serializable {
  private static final Tensor SINGLETON = Tensors.vector(1).unmodifiable();
  // ---
  protected final ScalarUnaryOperator windowFunction;
  protected final boolean isContinuous;

  /** @param windowFunction for evaluation in the interval [-1/2, +1/2] */
  protected BaseWindowSampler(ScalarUnaryOperator windowFunction) {
    this.windowFunction = windowFunction;
    isContinuous = Chop._03.allZero(windowFunction.apply(RationalScalar.HALF));
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
