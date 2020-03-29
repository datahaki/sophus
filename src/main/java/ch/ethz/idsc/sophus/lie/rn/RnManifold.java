// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;

public enum RnManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
