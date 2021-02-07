// code by jph
package ch.ethz.idsc.sophus.lie.dt;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum DtManifold {
  ;
  public static final LieExponential HS_EXP = //
      LieExponential.of(DtGroup.INSTANCE, DtExponential.INSTANCE);
}
