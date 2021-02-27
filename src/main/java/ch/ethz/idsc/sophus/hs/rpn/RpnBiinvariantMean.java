// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public enum RpnBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = of(Chop._14);

  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(RpnManifold.INSTANCE, chop);
  }
}
