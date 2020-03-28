// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;

public enum Se2CoveringBiinvariantMeanDefect {
  ;
  public static final MeanDefect INSTANCE = BiinvariantMeanDefect.of(Se2CoveringManifold.INSTANCE);
}
