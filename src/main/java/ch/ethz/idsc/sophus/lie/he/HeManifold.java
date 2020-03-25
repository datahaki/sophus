// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum HeManifold {
  ;
  public static final FlattenLogManifold INSTANCE = //
      LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog);
}
