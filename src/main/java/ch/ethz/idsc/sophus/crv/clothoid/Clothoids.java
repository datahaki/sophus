// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference: U. Reif slides
 * 
 * @param p vector of the form {px, py, pa}
 * @param q vector of the form {qx, qy, qa} */
public abstract class Clothoids implements ClothoidInterface, ClothoidQuadratic {
  @Override // from ClothoidInterface
  public final Clothoid curve(Tensor p, Tensor q) {
    ClothoidContext clothoidContext = new ClothoidContext(p, q);
    return new ClothoidImpl( //
        clothoidContext.lieGroupElement, //
        lagrangeQuadratic(clothoidContext.b0(), clothoidContext.b1()), //
        clothoidContext.diff);
  }

  @Override // from BinaryAverage
  public final Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
