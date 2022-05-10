// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.lie.LieExponential;

public enum ScManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(ScGroup.INSTANCE);
}
