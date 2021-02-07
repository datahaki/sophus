// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum HeManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      HeGroup.INSTANCE, //
      HeExponential.INSTANCE);
}
