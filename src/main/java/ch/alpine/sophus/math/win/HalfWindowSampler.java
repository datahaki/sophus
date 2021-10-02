// code by ob
package ch.alpine.sophus.math.win;

import java.util.function.Function;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Cache;

/** samples a given window function uniformly in the interval [-1/2, 0] */
public class HalfWindowSampler extends BaseWindowSampler {
  private static final int CACHE_SIZE = 32;

  /** @param windowFunction for evaluation in the interval [-1/2, 0] */
  public static Function<Integer, Tensor> of(ScalarUnaryOperator windowFunction) {
    return Cache.of(new HalfWindowSampler(windowFunction), CACHE_SIZE);
  }

  // ---
  private HalfWindowSampler(ScalarUnaryOperator windowFunction) {
    super(windowFunction);
  }

  @Override // from BaseWindowSampler
  protected Tensor samples(int length) {
    return isContinuous //
        ? Tensor.of(Subdivide.of(RationalScalar.HALF.negate(), RealScalar.ZERO, length) //
            .map(windowFunction) //
            .stream().skip(1).limit(length))
        : Subdivide.of(RationalScalar.HALF.negate(), RealScalar.ZERO, length - 1) //
            .map(windowFunction);
  }
}
