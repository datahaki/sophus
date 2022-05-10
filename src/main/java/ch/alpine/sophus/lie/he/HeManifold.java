// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.lie.LieExponential;

public enum HeManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(HeGroup.INSTANCE);
}
