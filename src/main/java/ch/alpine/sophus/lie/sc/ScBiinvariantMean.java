// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.WeightedGeometricMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.GeometricMean;

/** 1-parameter Scaling Group (R+, *)
 * 
 * @see GeometricMean
 * @see TdBiinvariantMean
 * @see WeightedGeometricMean */
public enum ScBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    return ScGroup.INSTANCE.exp(weights.dot(Tensor.of(sequence.stream().map(ScGroup.INSTANCE::log))));
  }
}
