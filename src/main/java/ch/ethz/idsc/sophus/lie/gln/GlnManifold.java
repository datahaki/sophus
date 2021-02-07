// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum GlnManifold {
  ;
  public static final LieExponential INSTANCE = //
      LieExponential.of(GlnGroup.INSTANCE, GlnExponential.INSTANCE);
}
