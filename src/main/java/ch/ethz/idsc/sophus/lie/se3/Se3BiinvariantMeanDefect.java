// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum Se3BiinvariantMeanDefect {
  ;
  public static final MeanDefect INSTANCE = //
      BiinvariantMeanDefect.of(LieFlattenLogManifold.of(Se3Group.INSTANCE, Se3Exponential.INSTANCE::log));
}
