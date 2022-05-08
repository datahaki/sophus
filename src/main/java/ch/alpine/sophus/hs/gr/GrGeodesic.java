// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.hs.HsGeodesic;

public enum GrGeodesic {
  ;
  public static final GeodesicSpace INSTANCE = new HsGeodesic(GrManifold.INSTANCE);
}
