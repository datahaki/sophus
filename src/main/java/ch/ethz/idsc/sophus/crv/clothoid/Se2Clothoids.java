// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.se2.Se2Group;
import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference: U. Reif slides
 * 
 * @param p vector of the form {px, py, pa}
 * @param q vector of the form {qx, qy, qa} */
public enum Se2Clothoids implements GeodesicInterface {
  INSTANCE;

  @Override // from ParametricCurve
  public Clothoid curve(Tensor p, Tensor q) {
    LieGroupElement lieGroupElement = Se2Group.INSTANCE.element(p);
    Tensor _q = lieGroupElement.inverse().combine(q);
    Tensor diff = _q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    // ---
    Scalar b0 = So2.MOD.apply(da.negate()); // normal form T0 == b0
    Scalar b1 = So2.MOD.apply(_q.Get(2).subtract(da)); // normal form T1 == b1
    // ---
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new ClothoidImpl(lieGroupElement, LagrangeQuadratic.interp(b0, bm, b1), diff);
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
