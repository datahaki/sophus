// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference: U. Reif slides
 * 
 * @param p vector of the form {px, py, pa}
 * @param q vector of the form {qx, qy, qa} */
public enum Se2CoveringClothoids implements ClothoidInterface {
  INSTANCE;

  @Override // from ParametricCurve
  public Clothoid curve(Tensor p, Tensor q) {
    LieGroupElement lieGroupElement = Se2CoveringGroup.INSTANCE.element(p);
    Tensor _q = lieGroupElement.inverse().combine(q);
    Tensor diff = _q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    // ---
    Scalar b0 = da.negate();
    Scalar b1 = _q.Get(2).subtract(da);
    // ---
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new ClothoidImpl(lieGroupElement, LagrangeQuadratic.interp(b0, bm, b1), diff);
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
