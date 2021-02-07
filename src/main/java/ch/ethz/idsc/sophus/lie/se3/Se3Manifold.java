// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.LieExponential;

public enum Se3Manifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      Se3Group.INSTANCE, //
      Se3Exponential.INSTANCE);
}
