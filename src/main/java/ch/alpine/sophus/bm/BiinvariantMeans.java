// code by jph
package ch.alpine.sophus.bm;

import java.util.Objects;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** operator that computes a weighted mean for a given sequence of points
 * with fixed weights for example used in smoothing filters or subdivision. */
public enum BiinvariantMeans {
  ;
  /** @param biinvariantMean non-null
   * @param weights affine vector
   * @return
   * @throws Exception if given weights is not an affine vector */
  public static TensorUnaryOperator of(BiinvariantMean biinvariantMean, Tensor weights) {
    Objects.requireNonNull(biinvariantMean);
    AffineQ.require(weights);
    return sequence -> biinvariantMean.mean(sequence, weights);
  }
}
