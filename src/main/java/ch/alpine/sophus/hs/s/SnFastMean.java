// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnBiinvariantMean} */
public enum SnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = SnPhongMean.INSTANCE.mean(sequence, weights);
    return new MeanDefect(sequence, weights, new SnExponential(mean)).shifted();
  }
}
