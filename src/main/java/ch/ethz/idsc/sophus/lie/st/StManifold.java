// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum StManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::vectorLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(StGroup.INSTANCE, StExponential.INSTANCE);
}
