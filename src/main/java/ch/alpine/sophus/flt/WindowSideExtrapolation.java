// code by ob, jph
package ch.alpine.sophus.flt;

import java.io.Serializable;
import java.util.function.Function;

import ch.alpine.sophus.math.win.HalfWindowSampler;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Cache;

/** BiinvariantMeanCenter projects a uniform sequence of points to their extrapolate
 * with each point weighted as provided by an external function. */
public class WindowSideExtrapolation implements Function<Integer, Tensor>, Serializable {
  /** @param windowFunction non-null
   * @return
   * @throws Exception if either input parameter is null */
  public static Function<Integer, Tensor> of(ScalarUnaryOperator windowFunction) {
    return Cache.of(new WindowSideExtrapolation(windowFunction), 32);
  }

  /***************************************************/
  private final Function<Integer, Tensor> halfWindowSampler;

  /* package */ WindowSideExtrapolation(ScalarUnaryOperator windowFunction) {
    halfWindowSampler = HalfWindowSampler.of(windowFunction);
  }

  // Assumes uniformly sampled signal!
  @Override
  public Tensor apply(Integer t) {
    Tensor weights = halfWindowSampler.apply(t);
    Tensor chronological = Range.of(0, weights.length());
    Scalar distance = RealScalar.of(weights.length() - 1).subtract(weights.dot(chronological));
    Tensor extrapolatory = weights.extract(0, weights.length() - 1).negate();
    extrapolatory.append(RealScalar.ONE.subtract(Last.of(weights)).add(distance));
    return extrapolatory.divide(distance).unmodifiable();
  }
}