// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;

/** Hint: non-linear, non conform with {@link SnGeodesic}
 * Do not use! */
public enum SnGlobalBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return Vector2Norm.NORMALIZE.apply(weights.dot(sequence));
  }
}
