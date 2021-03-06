// code by jph
package ch.ethz.idsc.sophus.bm;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
