// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.tensor.sca.Chop;

public enum GrBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = of(Chop._10);

  /** @param chop */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(GrManifold.INSTANCE, chop);
  }
}
