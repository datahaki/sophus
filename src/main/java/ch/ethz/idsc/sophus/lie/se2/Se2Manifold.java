// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;

public enum Se2Manifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      LieExponential.of(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE);
}
