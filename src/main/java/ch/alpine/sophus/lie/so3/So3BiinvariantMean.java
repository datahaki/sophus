// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.lie.so.SoPhongMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;

/** Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.3, 2012 */
public enum So3BiinvariantMean implements BiinvariantMean {
  INSTANCE;

  private static final BiinvariantMean BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(So3Group.INSTANCE, Tolerance.CHOP, SoPhongMean.INSTANCE);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return BIINVARIANT_MEAN.mean(sequence, weights);
  }
}
