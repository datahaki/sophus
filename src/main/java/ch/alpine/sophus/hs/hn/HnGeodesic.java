// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.hs.HsGeodesic;

/** hyperboloid model with fast midpoint computation */
public enum HnGeodesic {
  ;
  public static final GeodesicSpace INSTANCE = new HsGeodesic(HnManifold.INSTANCE);
}
