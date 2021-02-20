// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

/** Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.3, 2012 */
public enum So3BiinvariantMean implements BiinvariantMean {
  INSTANCE;

  private static final BiinvariantMean BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Chop._12);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return BIINVARIANT_MEAN.mean(sequence, weights);
  }
}
