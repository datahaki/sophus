// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.lie.LieExponential;

public enum R2SManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      R2SGroup.INSTANCE, //
      R2SExponential.INSTANCE);
}
