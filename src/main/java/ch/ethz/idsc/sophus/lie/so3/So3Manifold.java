// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum So3Manifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::flattenLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(So3Group.INSTANCE, So3Exponential.INSTANCE);
}
