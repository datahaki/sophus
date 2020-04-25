// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public class SpdBiinvariantMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = of(Chop._10);

  /** @param chop */
  public static BiinvariantMean of(Chop chop) {
    return new SpdBiinvariantMean(chop);
  }

  /***************************************************/
  private SpdBiinvariantMean(Chop chop) {
    super(SpdManifold.INSTANCE, chop);
  }
}
