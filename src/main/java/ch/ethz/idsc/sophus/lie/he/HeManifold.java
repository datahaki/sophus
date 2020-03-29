// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum HeManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(HeGroup.INSTANCE, HeExponential.INSTANCE);
}
