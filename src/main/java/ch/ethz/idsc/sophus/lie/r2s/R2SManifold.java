// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum R2SManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(R2SGroup.INSTANCE, R2SExponential.INSTANCE::vectorLog);
  public static final LieExponential HS_EXP = //
      LieExponential.of(R2SGroup.INSTANCE, R2SExponential.INSTANCE);
}
