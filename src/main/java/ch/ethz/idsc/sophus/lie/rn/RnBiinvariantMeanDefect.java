// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanDefect;

public enum RnBiinvariantMeanDefect {
  ;
  public static final BiinvariantMeanDefect INSTANCE = //
      new BiinvariantMeanDefect(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
