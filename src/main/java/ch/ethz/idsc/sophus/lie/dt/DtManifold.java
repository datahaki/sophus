// code by jph
package ch.ethz.idsc.sophus.lie.dt;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum DtManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      DtGroup.INSTANCE, //
      DtExponential.INSTANCE);
}
