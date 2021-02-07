// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.son.SonGroup;

public enum So3Manifold {
  ;
  public static final LieExponential INSTANCE = //
      LieExponential.of(SonGroup.INSTANCE, So3Exponential.INSTANCE);
}
