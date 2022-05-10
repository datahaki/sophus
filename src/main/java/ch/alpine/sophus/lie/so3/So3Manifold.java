// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.lie.LieExponential;

public enum So3Manifold {
  ;
  // TODO SOPHUS use SoTransport.INSTANCE
  public static final LieExponential INSTANCE = LieExponential.of(So3Group.INSTANCE);
}
