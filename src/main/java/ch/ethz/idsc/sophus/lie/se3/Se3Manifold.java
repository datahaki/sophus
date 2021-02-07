// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum Se3Manifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(Se3Group.INSTANCE, Se3Exponential.INSTANCE::vectorLog);
  public static final LieExponential HS_EXP = //
      LieExponential.of(Se3Group.INSTANCE, Se3Exponential.INSTANCE);
}
