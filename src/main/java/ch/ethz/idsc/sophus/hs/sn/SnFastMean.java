// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnBiinvariantMean} */
public enum SnFastMean implements BiinvariantMean {
  INSTANCE;

  private static final MeanDefect MEAN_DEFECT = new MeanDefect(SnManifold.INSTANCE);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor initial = SnPhongMean.INSTANCE.mean(sequence, weights);
    return new SnExponential(initial).exp(MEAN_DEFECT.defect(sequence, weights, initial));
  }
}
