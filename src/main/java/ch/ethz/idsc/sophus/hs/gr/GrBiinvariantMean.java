// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public enum GrBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = of(Chop._10);

  /** @param chop */
  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(GrManifold.INSTANCE, chop);
  }
}
