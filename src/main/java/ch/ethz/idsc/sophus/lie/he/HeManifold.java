// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;

public enum HeManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(HeGroup.INSTANCE, HeExponential.INSTANCE);
}
