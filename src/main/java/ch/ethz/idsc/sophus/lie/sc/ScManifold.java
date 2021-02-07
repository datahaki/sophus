// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum ScManifold {
  ;
  public static final LieExponential INSTANCE = //
      LieExponential.of(ScGroup.INSTANCE, ScExponential.INSTANCE);
}
