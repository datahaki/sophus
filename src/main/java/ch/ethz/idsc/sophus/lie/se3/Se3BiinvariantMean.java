// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;

public enum Se3BiinvariantMean implements BiinvariantMean {
  INSTANCE;

  private static final IterativeBiinvariantMean BIINVARIANT_MEAN_IMPLICIT = //
      new IterativeBiinvariantMean(Se3Manifold.HS_EXP);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return BIINVARIANT_MEAN_IMPLICIT.apply(sequence, weights).get();
  }
}
