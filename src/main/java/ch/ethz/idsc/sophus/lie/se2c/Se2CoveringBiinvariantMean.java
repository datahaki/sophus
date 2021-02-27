// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.so2c.So2CoveringBiinvariantMean;

public enum Se2CoveringBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = //
      Se2UniversalBiinvariantMean.covering(So2CoveringBiinvariantMean.INSTANCE);
}
