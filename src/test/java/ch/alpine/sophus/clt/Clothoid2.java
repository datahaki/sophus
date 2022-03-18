// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.sophus.ref.d1.LaneRiesenfeldCurveSubdivision;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** clothoid2 factory
 *
 * <p>The curvature of the provided curve resembles that of a clothoid.
 * The curve does not interpolate the tangents at the end points p and q in general.
 *
 * <p>In order to obtain samples of a clothoid that interpolates p and q the
 * recommended method is to use {@link LaneRiesenfeldCurveSubdivision} with
 * Clothoid2 and degrees 1 or 3. */
/* package */ enum Clothoid2 implements Geodesic {
  INSTANCE;

  @Override // from GeodesicInterface
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    return new ClothoidCurve2(p, q);
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
