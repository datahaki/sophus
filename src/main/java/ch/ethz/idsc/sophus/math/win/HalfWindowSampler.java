// code by ob
package ch.ethz.idsc.sophus.math.win;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.ext.Cache;

/** samples a given window function uniformly in the interval [-1/2, 0] */
public class HalfWindowSampler extends BaseWindowSampler {
  /** @param windowFunction for evaluation in the interval [-1/2, 0] */
  public static Function<Integer, Tensor> of(ScalarUnaryOperator windowFunction) {
    return Cache.of(new HalfWindowSampler(windowFunction), 32);
  }

  /***************************************************/
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
