// code by jph
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.lie.LieExponential;

public enum DtManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      DtGroup.INSTANCE, //
      DtExponential.INSTANCE);
}
