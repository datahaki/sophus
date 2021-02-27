// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum GlManifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      GlGroup.INSTANCE, //
      GlExponential.INSTANCE);
}
