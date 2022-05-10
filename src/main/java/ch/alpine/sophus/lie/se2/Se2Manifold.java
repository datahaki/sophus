// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.lie.LieExponential;

public enum Se2Manifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(Se2Group.INSTANCE);
}
