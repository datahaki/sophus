// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.dt.DtBiinvariantMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.WeightedGeometricMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.GeometricMean;

/** 1-parameter Scaling Group (R+, *)
 * 
 * @see GeometricMean
 * @see DtBiinvariantMean
 * @see WeightedGeometricMean */
public enum ScBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    return ScExponential.INSTANCE.exp(weights.dot(Tensor.of(sequence.stream().map(ScExponential.INSTANCE::log))));
  }
}
