// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum RnManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      LieExponential.of(RnGroup.INSTANCE, RnExponential.INSTANCE);
}
