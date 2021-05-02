// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.tensor.sca.Chop;

public enum RpnBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = of(Chop._14);

  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(RpnManifold.INSTANCE, chop);
  }
}
