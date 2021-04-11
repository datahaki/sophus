// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.math.Geodesic;

public class GrGeodesic extends HsGeodesic {
  public static final Geodesic INSTANCE = new GrGeodesic();

  /***************************************************/
  private GrGeodesic() {
    super(GrManifold.INSTANCE);
  }
}
