// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;
import ch.ethz.idsc.sophus.lie.son.SonGroup;

public enum So3Manifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(SonGroup.INSTANCE, So3Exponential.INSTANCE::log);
  public static final LieExponential HS_EXP = //
      LieExponential.of(SonGroup.INSTANCE, So3Exponential.INSTANCE);
}
