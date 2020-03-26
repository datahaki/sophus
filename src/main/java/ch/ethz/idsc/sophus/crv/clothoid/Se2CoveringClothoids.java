// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.crv.subdiv.LaneRiesenfeldCurveSubdivision;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** clothoid3 factory
 *
 * <p>The curvature of the provided curve resembles that of a clothoid.
 * The curve does not interpolate the tangents at the end points p and q in general.
 *
 * <p>In order to obtain samples of a clothoid that interpolates p and q the
 * recommended method is to use {@link LaneRiesenfeldCurveSubdivision} with
 * Clothoids and degrees 1 or 3. */
public enum Se2CoveringClothoids implements GeodesicInterface {
  INSTANCE;

  @Override // from GeodesicInterface
  public Clothoid curve(Tensor p, Tensor _q) {
    LieGroupElement lieGroupElement = Se2CoveringGroup.INSTANCE.element(p);
    Tensor q = lieGroupElement.inverse().combine(_q);
    Tensor diff = q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    Scalar qa = q.Get(2);
    Scalar b0 = da.negate(); // normal form T0 == b0
    Scalar b1 = qa.subtract(da); // normal form T1 == b1
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new ClothoidImpl(lieGroupElement, LagrangeQuadratic.interp(b0, bm, b1), diff);
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
