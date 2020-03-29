// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum StManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(StGroup.INSTANCE, StExponential.INSTANCE);
}
