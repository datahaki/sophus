// code by jph
package ch.ethz.idsc.sophus.lie.dt;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum DtManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(DtGroup.INSTANCE, DtExponential.INSTANCE::vectorLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(DtGroup.INSTANCE, DtExponential.INSTANCE);
}
