// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.lie.LieExponential;

public enum RnManifold {
  ;
  public static final LieExponential INSTANCE = //
      LieExponential.of(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
