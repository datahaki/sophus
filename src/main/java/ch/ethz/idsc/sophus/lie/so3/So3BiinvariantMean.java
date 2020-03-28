// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.BiinvariantMeanImplicit;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.3, 2012 */
public enum So3BiinvariantMean implements BiinvariantMean {
  INSTANCE;

  private static final BiinvariantMeanImplicit BIINVARIANT_MEAN_IMPLICIT = //
      new BiinvariantMeanImplicit(So3Group.INSTANCE, So3Exponential.INSTANCE);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return BIINVARIANT_MEAN_IMPLICIT.apply(sequence, weights).get();
  }
}
