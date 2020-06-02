// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum RnManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      LieExponential.of(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
