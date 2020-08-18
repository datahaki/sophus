// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference: U. Reif slides
 * 
 * maps to SE(2) or SE(2) Covering
 * 
 * @param p vector of the form {px, py, pa}
 * @param q vector of the form {qx, qy, qa} */
public final class ClothoidBuilderImpl implements ClothoidBuilder, Serializable {
  private final ClothoidQuadratic clothoidQuadratic;

  public ClothoidBuilderImpl(ClothoidQuadratic clothoidQuadratic) {
    this.clothoidQuadratic = Objects.requireNonNull(clothoidQuadratic);
  }

  @Override // from ClothoidInterface
  public Clothoid curve(Tensor p, Tensor q) {
    ClothoidContext clothoidContext = new ClothoidContext(p, q);
    LagrangeQuadratic lagrangeQuadratic = //
        clothoidQuadratic.lagrangeQuadratic(clothoidContext.b0(), clothoidContext.b1());
    return new ClothoidImpl( //
        clothoidContext.lieGroupElement, //
        lagrangeQuadratic, //
        clothoidContext.diff);
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
