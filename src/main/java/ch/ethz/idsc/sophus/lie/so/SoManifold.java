// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum SoManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      SoGroup.INSTANCE, //
      SoExponential.INSTANCE);
}
