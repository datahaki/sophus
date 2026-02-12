// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.bm.MeanEstimate;
import ch.alpine.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnBiinvariantMean} */
public enum SnFastMean implements MeanEstimate {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor estimate(Tensor sequence, Tensor weights) {
    Tensor mean = SnPhongMean.INSTANCE.estimate(sequence, weights);
    return MeanDefect.of(sequence, weights, new SnExponential(mean)).shifted();
  }
}
