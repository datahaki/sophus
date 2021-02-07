// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum SonManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      SonGroup.INSTANCE, //
      SonExponential.INSTANCE);
}
