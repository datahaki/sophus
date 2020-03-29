// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;

public enum Se3BiinvariantMeanDefect {
  ;
  public static final MeanDefect INSTANCE = BiinvariantMeanDefect.of(Se3Manifold.HS_EXP);
}
