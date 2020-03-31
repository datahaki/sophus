// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.sn.SnBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnBiinvariantMean} */
public enum HnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor initial = HnPhongMean.INSTANCE.mean(sequence, weights);
    return new HnExponential(initial).exp(HnMeanDefect.INSTANCE.defect(sequence, weights, initial));
  }
}
