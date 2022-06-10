// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;

public class GeodesicBSplineInterpolation extends AbstractBSplineInterpolation {
  /** @param geodesicSpace
   * @param degree
   * @param target */
  public GeodesicBSplineInterpolation(GeodesicSpace geodesicSpace, int degree, Tensor target) {
    super(geodesicSpace, degree, target);
  }

  @Override // from AbstractBSplineInterpolation
  protected Tensor move(Tensor p, Tensor e, Tensor t) {
    Tensor pt = geodesicSpace.midpoint(p, t);
    Tensor et = geodesicSpace.midpoint(e, t);
    Tensor tf = geodesicSpace.split(et, pt, RealScalar.TWO); // transfer
    return geodesicSpace.split(p, tf, RealScalar.TWO); // push
  }
}
