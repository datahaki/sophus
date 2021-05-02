// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.tensor.sca.Chop;

public enum HnBiinvariantMean {
  ;
  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(HnManifold.INSTANCE, chop, HnPhongMean.INSTANCE);
  }
}
