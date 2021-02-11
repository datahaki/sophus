// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public enum HnBiinvariantMean {
  ;
  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(HnManifold.INSTANCE, chop, HnPhongMean.INSTANCE);
  }
}
