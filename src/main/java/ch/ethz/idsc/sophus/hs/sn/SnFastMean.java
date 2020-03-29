// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnMean} */
public enum SnFastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = SnPhongMean.INSTANCE.mean(sequence, weights);
    return new SnExponential(mean).exp(SnMeanDefect.INSTANCE.defect(sequence, weights, mean));
  }
}
