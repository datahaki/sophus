// code by ob, jph
package ch.alpine.sophus.flt.bm;

import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Cache;

/** BiinvariantMeanCenter projects a sequence of points to their barycenter
 * with each point weighted as provided by an external function. */
public class BiinvariantMeanCenter implements TensorUnaryOperator {
  private static final int CACHE_SIZE = 64;

  /** @param biinvariantMean non-null
   * @param function non-null
   * @return operator that maps a sequence of odd number of points to their barycenter
   * @throws Exception if either input parameter is null */
  public static TensorUnaryOperator of(BiinvariantMean biinvariantMean, Function<Integer, Tensor> function) {
    return new BiinvariantMeanCenter(biinvariantMean, Cache.of(function, CACHE_SIZE));
  }

  /** @param biinvariantMean non-null
   * @param windowFunction non-null
   * @return operator that maps a sequence of odd number of points to their barycenter
   * @throws Exception if either input parameter is null */
  public static TensorUnaryOperator of(BiinvariantMean biinvariantMean, ScalarUnaryOperator windowFunction) {
    return new BiinvariantMeanCenter(biinvariantMean, UniformWindowSampler.of(windowFunction));
  }

  // ---
  private final BiinvariantMean biinvariantMean;
  private final Function<Integer, Tensor> function;

  private BiinvariantMeanCenter(BiinvariantMean biinvariantMean, Function<Integer, Tensor> function) {
    this.biinvariantMean = Objects.requireNonNull(biinvariantMean);
    this.function = function;
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    return biinvariantMean.mean(tensor, function.apply(tensor.length()));
  }
}
