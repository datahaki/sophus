// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.hs.sn.SnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.so.SoPhongMean;
import ch.ethz.idsc.tensor.Tensor;

/** Phong mean with 1-step correction towards {@link SnBiinvariantMean} */
public enum So3FastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = SoPhongMean.INSTANCE.mean(sequence, weights);
    return new MeanDefect(sequence, weights, So3Manifold.INSTANCE.exponential(mean)).shifted();
  }
}
