// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.ext.Cache;

/** samples a given window function uniformly in the interval [-1/2, +1/2] */
public class UniformWindowSampler extends BaseWindowSampler {
  /** @param windowFunction for evaluation in the interval [-1/2, +1/2]
   * @return */
  public static Function<Integer, Tensor> of(ScalarUnaryOperator windowFunction) {
    return Cache.of(new UniformWindowSampler(windowFunction), 32);
  }

  /***************************************************/
  private UniformWindowSampler(ScalarUnaryOperator windowFunction) {
    super(windowFunction);
  }

  @Override // from BaseWindowSampler
  protected Tensor samples(int length) {
    return isContinuous //
        ? Tensor.of(Subdivide.of(RationalScalar.HALF.negate(), RationalScalar.HALF, length + 1) //
            .map(windowFunction) //
            .stream().skip(1).limit(length))
        : Subdivide.of(RationalScalar.HALF.negate(), RationalScalar.HALF, length - 1) //
            .map(windowFunction);
  }
}
