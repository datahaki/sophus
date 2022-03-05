// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.sophus.hs.HsGeodesic;

/** hyperboloid model with fast midpoint computation */
public enum HnGeodesic {
  ;
  public static final Geodesic INSTANCE = new HsGeodesic(HnManifold.INSTANCE);
}
