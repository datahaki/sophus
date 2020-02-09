// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanDefect;

public enum HeBiinvariantMeanDefect {
  ;
  public static final BiinvariantMeanDefect INSTANCE = //
      new BiinvariantMeanDefect(HeGroup.INSTANCE, HeExponential.INSTANCE);
}
