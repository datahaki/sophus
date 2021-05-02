// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.decim.HsLineDistance;
import ch.alpine.sophus.decim.LineDistance;

public enum RnLineDistance {
  ;
  public static final LineDistance INSTANCE = new HsLineDistance(RnManifold.INSTANCE);
}
