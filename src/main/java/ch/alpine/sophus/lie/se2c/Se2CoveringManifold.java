// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.lie.LieExponential;

public enum Se2CoveringManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of(Se2CoveringGroup.INSTANCE);
}
