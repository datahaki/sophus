// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsGeodesic;
import ch.ethz.idsc.sophus.math.GeodesicInterface;

/** hyperboloid model with fast midpoint computation */
public class HnGeodesic extends HsGeodesic {
  public static final GeodesicInterface INSTANCE = new HnGeodesic();

  /***************************************************/
  private HnGeodesic() {
    super(HnManifold.INSTANCE);
  }
  //
  // @Override // from MidpointInterface
  // public Tensor midpoint(Tensor p, Tensor q) {
  // return HnProjection.INSTANCE.apply(p.add(q)); // TODO this projection is not the right op here
  // need to project by synchronized scaling of xs and xn part of x = p+q
  // }
}
