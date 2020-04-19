// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Tensor;

/** hyperboloid model
 * 
 * fast midpoint computation */
public class HnGeodesic extends HsGeodesic {
  public static final GeodesicInterface INSTANCE = new HnGeodesic();

  /***************************************************/
  private HnGeodesic() {
    super(HnManifold.INSTANCE);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return HnProjection.INSTANCE.apply(p.add(q));
  }
}