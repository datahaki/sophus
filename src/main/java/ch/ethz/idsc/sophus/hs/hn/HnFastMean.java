// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link HnBiinvariantMean} */
public enum HnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return new MeanDefect(sequence, weights, new HnExponential(HnPhongMean.INSTANCE.mean(sequence, weights))).shifted();
  }
}
