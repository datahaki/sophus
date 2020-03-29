// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;

public enum Se2CoveringManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE);
}
