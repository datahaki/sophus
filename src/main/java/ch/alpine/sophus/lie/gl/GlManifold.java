// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.lie.LieExponential;

public enum GlManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(GlGroup.INSTANCE);
}
