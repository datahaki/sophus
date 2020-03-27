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
public abstract class Clothoids implements ClothoidInterface {
  @Override // from ClothoidInterface
  public final Clothoid curve(Tensor p, Tensor q) {
    LieGroupElement lieGroupElement = Se2CoveringGroup.INSTANCE.element(p);
    Tensor _q = lieGroupElement.inverse().combine(q);
    Tensor diff = _q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    return new ClothoidImpl( //
        lieGroupElement, //
        lagrangeQuadratic(da.negate(), _q.Get(2).subtract(da)), //
        diff);
  }

  @Override // from BinaryAverage
  public final Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }

  /** @param b0 angle
   * @param b1 angle
   * @return */
  protected abstract LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1);
}
