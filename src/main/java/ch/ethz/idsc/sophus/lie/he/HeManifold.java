// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum HeManifold {
  ;
  public static final LieExponential HS_EXP = //
      LieExponential.of(HeGroup.INSTANCE, HeExponential.INSTANCE);
}
