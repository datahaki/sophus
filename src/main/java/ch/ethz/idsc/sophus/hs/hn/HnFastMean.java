// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.hs.sn.SnFastMean;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link HnBiinvariantMean} 
 * 
 * @see SnFastMean */
public enum HnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = HnPhongMean.INSTANCE.mean(sequence, weights);
    return new MeanDefect(sequence, weights, new HnExponential(mean)).shifted();
  }
}
