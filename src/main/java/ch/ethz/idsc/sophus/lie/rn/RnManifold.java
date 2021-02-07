// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum RnManifold {
  ;
  public static final LieExponential INSTANCE = //
      LieExponential.of(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
