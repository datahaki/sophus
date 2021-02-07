// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum R2SManifold {
  ;
  public static final LieExponential HS_EXP = //
      LieExponential.of(R2SGroup.INSTANCE, R2SExponential.INSTANCE);
}
