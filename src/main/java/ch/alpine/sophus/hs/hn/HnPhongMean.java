// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.sn.SnPhongMean;
import ch.alpine.tensor.Tensor;

/** @see SnPhongMean */
public enum HnPhongMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return HnProjection.INSTANCE.apply(weights.dot(sequence));
  }
}
