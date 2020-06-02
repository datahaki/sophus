// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum Se2CoveringManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      LieExponential.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE);
}
