// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.lie.so.SoPhongMean;
import ch.alpine.tensor.Tensor;

/** Phong mean with 1-step correction towards SoBiinvariantMean */
public enum So3FastMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = SoPhongMean.INSTANCE.mean(sequence, weights);
    return new MeanDefect(sequence, weights, So3Group.INSTANCE.exponential(mean)).shifted();
  }
}
