// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.math.Geodesic;

/** hyperboloid model with fast midpoint computation */
public enum HnGeodesic {
  ;
  public static final Geodesic INSTANCE = new HsGeodesic(HnManifold.INSTANCE);
}
