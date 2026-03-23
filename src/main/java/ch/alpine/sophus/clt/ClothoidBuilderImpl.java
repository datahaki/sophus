// code by jph
package ch.alpine.sophus.clt;

import java.io.Serializable;

import ch.alpine.sophus.clt.mid.ClothoidQuadratic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.MathematicaFormat;

/** Reference: U. Reif slides
 * 
 * maps to SE(2) or SE(2) Covering */
public record ClothoidBuilderImpl(ClothoidQuadratic clothoidQuadratic, ClothoidIntegration clothoidIntegration) //
    implements ClothoidBuilder, Serializable {
  /** @param clothoidContext
   * @return */
  public Clothoid from(ClothoidContext clothoidContext) {
    LagrangeQuadratic lagrangeQuadratic = //
        clothoidQuadratic.lagrangeQuadratic(clothoidContext.b0(), clothoidContext.b1());
    ClothoidIntegral clothoidIntegral = clothoidIntegration.clothoidIntegral(lagrangeQuadratic);
    return new ClothoidImpl( //
        clothoidContext.p(), //
        clothoidIntegral, //
        clothoidContext.diff());
  }

  public static ClothoidBuilder custom(Scalar lambda, ClothoidIntegration clothoidIntegration) {
    return new ClothoidBuilderImpl(CustomClothoidQuadratic.of(lambda), clothoidIntegration);
  }

  @Override // from ClothoidBuilder
  public Clothoid curve(Tensor p, Tensor q) {
    return from(new ClothoidContext(p, q));
  }

  @Override
  public final String toString() {
    return MathematicaFormat.concise( //
        clothoidQuadratic.toString(), //
        clothoidIntegration.toString().substring(0, 1));
  }
}
