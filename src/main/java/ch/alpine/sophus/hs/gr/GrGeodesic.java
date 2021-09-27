// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.math.Geodesic;

public class GrGeodesic extends HsGeodesic {
  public static final Geodesic INSTANCE = new GrGeodesic();

  // ---
  private GrGeodesic() {
    super(GrManifold.INSTANCE);
  }
}
