// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum HeManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::vectorLog);
  public static final LieExponential HS_EXP = //
      LieExponential.of(HeGroup.INSTANCE, HeExponential.INSTANCE);
}
