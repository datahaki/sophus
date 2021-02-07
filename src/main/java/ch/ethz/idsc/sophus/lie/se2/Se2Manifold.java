// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;

public enum Se2Manifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      Se2Group.INSTANCE, //
      Se2CoveringExponential.INSTANCE);
}
