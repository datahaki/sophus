// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.math.Geodesic;

public enum GrGeodesic {
  ;
  public static final Geodesic INSTANCE = new HsGeodesic(GrManifold.INSTANCE);
}
