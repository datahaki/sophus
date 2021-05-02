// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;

public enum Se2Manifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      Se2Group.INSTANCE, //
      Se2CoveringExponential.INSTANCE);
}
