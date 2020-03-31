// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;

public enum HnMeanDefect {
  ;
  public static final MeanDefect INSTANCE = BiinvariantMeanDefect.of(HnManifold.INSTANCE);
}
