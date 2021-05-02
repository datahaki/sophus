// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.lie.so.SoGroup;

public enum So3Manifold {
  ;
  public static final LieExponential INSTANCE = LieExponential.of( //
      SoGroup.INSTANCE, //
      So3Exponential.INSTANCE);
}
