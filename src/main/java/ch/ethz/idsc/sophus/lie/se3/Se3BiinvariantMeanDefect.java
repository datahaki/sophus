// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanDefect;

public enum Se3BiinvariantMeanDefect {
  ;
  public static final BiinvariantMeanDefect INSTANCE = new BiinvariantMeanDefect( //
      Se3Group.INSTANCE, //
      Se3Exponential.INSTANCE);
}
