// code by jph
package ch.alpine.sophus.math.win;

import java.util.function.Function;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/** samples a given window function uniformly in the interval [-1/2, +1/2] */
public class UniformWindowSampler extends BaseWindowSampler {
  private static final int CACHE_SIZE = 32;
  private static final Clip CLIP = Clips.absolute(RationalScalar.HALF);

  /** @param windowFunction for evaluation in the interval [-1/2, +1/2]
   * @return */
  public static Function<Integer, Tensor> of(ScalarUnaryOperator windowFunction) {
    return Cache.of(new UniformWindowSampler(windowFunction), CACHE_SIZE);
  }

  // ---
  private UniformWindowSampler(ScalarUnaryOperator windowFunction) {
    super(windowFunction);
  }

  @Override // from BaseWindowSampler
  protected Tensor samples(int length) {
    return isContinuous //
        ? Tensor.of(Subdivide.increasing(CLIP, length + 1) //
            .map(windowFunction) //
            .stream().skip(1).limit(length))
        : Subdivide.increasing(CLIP, length - 1) //
            .map(windowFunction);
  }
}
