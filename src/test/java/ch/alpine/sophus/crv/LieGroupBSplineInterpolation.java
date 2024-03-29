// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BSplineInterpolation;

/** computation of control points that result in a limit curve
 * that interpolates given target points.
 * 
 * Hint: when target coordinates are specified in exact precision,
 * the iteration may involve computing fractions consisting of large
 * integers. Therefore, it is recommended to provide target points
 * in numeric precision.
 * 
 * @see BSplineInterpolation */
public final class LieGroupBSplineInterpolation extends AbstractBSplineInterpolation {
  private final LieGroup lieGroup;

  /** @param lieGroup
   * @param degree of underlying b-spline
   * @param target */
  public LieGroupBSplineInterpolation(LieGroup lieGroup, int degree, Tensor target) {
    super(lieGroup, degree, target);
    this.lieGroup = lieGroup;
  }

  @Override // from AbstractBSplineInterpolation
  protected Tensor move(Tensor p, Tensor e, Tensor t) {
    return lieGroup.element(p).combine(lieGroup.element(e).inverse().combine(t));
  }
}
