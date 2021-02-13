// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

/** Hint: non-linear, non conform with {@link SnGeodesic}
 * Do not use! */
public enum SnGlobalBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return VectorNorm2.NORMALIZE.apply(weights.dot(sequence));
  }
}
