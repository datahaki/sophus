// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.sophus.api.SplitInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;

public class GeodesicBSplineInterpolation extends AbstractBSplineInterpolation {
  /** @param splitInterface
   * @param degree
   * @param target */
  public GeodesicBSplineInterpolation(SplitInterface splitInterface, int degree, Tensor target) {
    super(splitInterface, degree, target);
  }

  @Override // from AbstractBSplineInterpolation
  protected Tensor move(Tensor p, Tensor e, Tensor t) {
    Tensor pt = splitInterface.midpoint(p, t);
    Tensor et = splitInterface.midpoint(e, t);
    Tensor tf = splitInterface.split(et, pt, RealScalar.TWO); // transfer
    return splitInterface.split(p, tf, RealScalar.TWO); // push
  }
}
