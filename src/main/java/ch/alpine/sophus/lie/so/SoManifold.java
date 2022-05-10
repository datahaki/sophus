// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.lie.LieExponential;

public enum SoManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(SoGroup.INSTANCE);
}
