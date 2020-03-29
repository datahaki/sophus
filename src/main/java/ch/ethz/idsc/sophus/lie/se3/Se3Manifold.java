// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum Se3Manifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential::flattenLog);
  public static final HsExponential HS_EXP = //
      LieExponential.of(Se3Group.INSTANCE, Se3Exponential.INSTANCE);
}
