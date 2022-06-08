// code by ureif
package ch.alpine.sophus.crv.clt.par;

import java.io.Serializable;

import ch.alpine.sophus.crv.clt.LagrangeQuadratic;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

/** The integral of exp i*clothoidQuadratic as suggested in U. Reif slides has the
 * property, that the values of the polynomial correspond to the tangent angle. */
/* package */ class AnalyticClothoidIntegral implements ClothoidIntegral, Serializable {
  private final ClothoidPartial clothoidPartial;
  private final Scalar one;

  public AnalyticClothoidIntegral(LagrangeQuadratic lagrangeQuadratic) {
    clothoidPartial = AnalyticClothoidPartial.of(lagrangeQuadratic);
    one = clothoidPartial.il(RealScalar.ONE);
  }

  @Override // from ClothoidIntegral
  public Scalar normalized(Scalar t) {
    return clothoidPartial.il(t).divide(one);
  }

  @Override // from ClothoidIntegral
  public Scalar one() {
    return one;
  }
}
