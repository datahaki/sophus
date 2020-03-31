// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.sn.SnPhongMean;
import ch.ethz.idsc.tensor.Tensor;

/** @see SnPhongMean */
public enum HnPhongMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return HnProjection.INSTANCE.apply(weights.dot(sequence));
  }
}
