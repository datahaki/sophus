// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.BiinvariantMeanEquation;

public enum So3BiinvariantMeanEquation {
  ;
  public static final BiinvariantMeanEquation INSTANCE = //
      new BiinvariantMeanEquation(So3Group.INSTANCE, So3Exponential.INSTANCE);
}
