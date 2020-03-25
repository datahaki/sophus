// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

public enum SpdBarycentricCoordinates {
  ;
  public static final BarycentricCoordinate INSTANCE = HsBarycentricCoordinate.linear(SpdManifold.INSTANCE);
  public static final BarycentricCoordinate SQUARED = HsBarycentricCoordinate.smooth(SpdManifold.INSTANCE);
}
