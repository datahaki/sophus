// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.decim.HsLineDistance;
import ch.ethz.idsc.sophus.decim.LineDistance;

public enum RnLineDistance {
  ;
  public static final LineDistance INSTANCE = new HsLineDistance(RnManifold.INSTANCE);
}
