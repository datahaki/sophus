// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.lie.PointLieExponential;

public enum So3Manifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(So3Group.INSTANCE, So3Exponential.INSTANCE::log);
  public static final HsExponential HS_EXP = //
      PointLieExponential.of(So3Group.INSTANCE, So3Exponential.INSTANCE);
}
