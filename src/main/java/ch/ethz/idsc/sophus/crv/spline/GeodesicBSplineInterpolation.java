// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.sophus.math.SplitInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;

public class GeodesicBSplineInterpolation extends AbstractBSplineInterpolation {
  private static final long serialVersionUID = 9049210786655894429L;

  /** @param binaryAverage
   * @param degree
   * @param target */
  public GeodesicBSplineInterpolation(SplitInterface binaryAverage, int degree, Tensor target) {
    super(binaryAverage, degree, target);
  }

  @Override // from AbstractBSplineInterpolation
  protected Tensor move(Tensor p, Tensor e, Tensor t) {
    Tensor pt = splitInterface.midpoint(p, t);
    Tensor et = splitInterface.midpoint(e, t);
    Tensor tf = splitInterface.split(et, pt, RealScalar.TWO); // transfer
    return splitInterface.split(p, tf, RealScalar.TWO); // push
  }
}
