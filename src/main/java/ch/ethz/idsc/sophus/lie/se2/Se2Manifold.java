// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;

public enum Se2Manifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE);
}
