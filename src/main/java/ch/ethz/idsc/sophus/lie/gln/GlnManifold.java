// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;
import ch.ethz.idsc.sophus.lie.son.SonGroup;

public enum GlnManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(SonGroup.INSTANCE, GlnExponential.INSTANCE);
  public static final LieExponential HS_EXP = //
      LieExponential.of(SonGroup.INSTANCE, GlnExponential.INSTANCE);
}
