// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public final class SnBiinvariantMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new SnBiinvariantMean(Chop._14);

  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return new SnBiinvariantMean(chop);
  }

  /***************************************************/
  private SnBiinvariantMean(Chop chop) {
    super(SnManifold.INSTANCE, SnPhongMean.INSTANCE, chop);
  }
}
