// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.dt.DtBiinvariantMean;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.GeometricMean;
import ch.ethz.idsc.tensor.sca.Chop;

/** 1-parameter Scaling Group (R+, *)
 * 
 * @see GeometricMean
 * @see DtBiinvariantMean */
public enum ScBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights, Chop._08);
    return ScExponential.INSTANCE.exp(weights.dot(Tensor.of(sequence.stream().map(ScExponential.INSTANCE::log))));
  }
}
