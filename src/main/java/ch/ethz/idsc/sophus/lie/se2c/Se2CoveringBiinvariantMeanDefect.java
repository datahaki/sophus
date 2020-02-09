// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanDefect;

public enum Se2CoveringBiinvariantMeanDefect {
  ;
  public static final BiinvariantMeanDefect INSTANCE = //
      new BiinvariantMeanDefect(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE);
}
