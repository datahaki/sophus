// code by jph
package ch.alpine.sophus.clt;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.clt.mid.ClothoidQuadratic;
import ch.alpine.sophus.clt.par.ClothoidIntegral;
import ch.alpine.sophus.clt.par.ClothoidIntegration;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Reference: U. Reif slides
 * 
 * maps to SE(2) or SE(2) Covering
 * 
 * @param p vector of the form {px, py, pa}
 * @param q vector of the form {qx, qy, qa} */
public final class ClothoidBuilderImpl implements ClothoidBuilder, Serializable {
  private final ClothoidQuadratic clothoidQuadratic;
  private final ClothoidIntegration clothoidIntegration;

  public ClothoidBuilderImpl(ClothoidQuadratic clothoidQuadratic, ClothoidIntegration clothoidIntegration) {
    this.clothoidQuadratic = Objects.requireNonNull(clothoidQuadratic);
    this.clothoidIntegration = Objects.requireNonNull(clothoidIntegration);
  }

  @Override // from ClothoidBuilder
  public Clothoid curve(Tensor p, Tensor q) {
    return from(new ClothoidContext(p, q));
  }

  public Clothoid from(ClothoidContext clothoidContext) {
    LagrangeQuadratic lagrangeQuadratic = //
        clothoidQuadratic.lagrangeQuadratic(clothoidContext.b0(), clothoidContext.b1());
    ClothoidIntegral clothoidIntegral = clothoidIntegration.clothoidIntegral(lagrangeQuadratic);
    return new ClothoidImpl( //
        clothoidContext.lieGroupElement(), //
        lagrangeQuadratic, //
        clothoidIntegral, //
        clothoidContext.diff());
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar t) {
    return curve(p, q).apply(t);
  }
}
