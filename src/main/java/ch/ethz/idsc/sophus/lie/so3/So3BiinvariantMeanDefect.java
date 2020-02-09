// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanDefect;

public enum So3BiinvariantMeanDefect {
  ;
  public static final BiinvariantMeanDefect INSTANCE = //
      new BiinvariantMeanDefect(So3Group.INSTANCE, So3Exponential.INSTANCE);
}
