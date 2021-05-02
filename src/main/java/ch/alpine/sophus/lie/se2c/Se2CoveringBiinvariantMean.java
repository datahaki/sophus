// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.so2c.So2CoveringBiinvariantMean;

public enum Se2CoveringBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = //
      Se2UniversalBiinvariantMean.covering(So2CoveringBiinvariantMean.INSTANCE);
}
