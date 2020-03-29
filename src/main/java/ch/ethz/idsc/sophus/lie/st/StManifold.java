// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;

public enum StManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(StGroup.INSTANCE, StExponential.INSTANCE);
}
