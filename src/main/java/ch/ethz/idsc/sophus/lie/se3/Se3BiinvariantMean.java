// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.BiinvariantMeanImplicit;
import ch.ethz.idsc.tensor.Tensor;

public enum Se3BiinvariantMean implements BiinvariantMean {
  INSTANCE;

  private static final BiinvariantMeanImplicit BIINVARIANT_MEAN_IMPLICIT = //
      new BiinvariantMeanImplicit(Se3Group.INSTANCE, Se3Exponential.INSTANCE);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return BIINVARIANT_MEAN_IMPLICIT.apply(sequence, weights).get();
  }
}
