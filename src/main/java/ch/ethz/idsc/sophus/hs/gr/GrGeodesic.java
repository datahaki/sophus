// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Tensor;

public class GrGeodesic extends HsGeodesic {
  public static final GeodesicInterface INSTANCE = new GrGeodesic();

  /***************************************************/
  private GrGeodesic() {
    super(GrManifold.INSTANCE);
  }

  @Override
  public Tensor midpoint(Tensor p, Tensor q) {
    return new GrExponential(p).midpoint(q);
  }
}
