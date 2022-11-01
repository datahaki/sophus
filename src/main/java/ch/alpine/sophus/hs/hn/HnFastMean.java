// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.hs.sn.SnFastMean;
import ch.alpine.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link HnManifold}
 * 
 * @see HnPhongMean
 * @see SnFastMean */
public enum HnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = HnPhongMean.INSTANCE.mean(sequence, weights);
    return new MeanDefect(sequence, weights, new HnExponential(mean)).shifted();
  }
}
