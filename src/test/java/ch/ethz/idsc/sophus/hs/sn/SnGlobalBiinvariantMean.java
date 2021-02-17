// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

/** Hint: non-linear, non conform with {@link SnGeodesic}
 * Do not use! */
public enum SnGlobalBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return Vector2Norm.NORMALIZE.apply(weights.dot(sequence));
  }
}
