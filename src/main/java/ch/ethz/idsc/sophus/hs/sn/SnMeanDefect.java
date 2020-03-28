// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;

public enum SnMeanDefect {
  ;
  public static final MeanDefect INSTANCE = BiinvariantMeanDefect.of(SnManifold.INSTANCE);
}
