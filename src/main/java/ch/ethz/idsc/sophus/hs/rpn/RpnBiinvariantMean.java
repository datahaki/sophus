// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public final class RpnBiinvariantMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new RpnBiinvariantMean(Chop._14);

  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return new RpnBiinvariantMean(chop);
  }

  /***************************************************/
  private RpnBiinvariantMean(Chop chop) {
    super(RpnManifold.INSTANCE, chop);
  }
}
