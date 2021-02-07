// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum Se2CoveringManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      Se2CoveringGroup.INSTANCE, //
      Se2CoveringExponential.INSTANCE);
}
