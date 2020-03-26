// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
public final class ClothoidImpl implements Clothoid {
  private final LieGroupElement lieGroupElement;
  private final LagrangeQuadratic lagrangeQuadratic;
  private final Tensor diff;
  private final ClothoidIntegral clothoidIntegral;
  private final Scalar length;
  private final Scalar da;

  /** @param lieGroupElement
   * @param lagrangeQuadratic
   * @param diff */
  public ClothoidImpl(LieGroupElement lieGroupElement, LagrangeQuadratic lagrangeQuadratic, Tensor diff) {
    this.lieGroupElement = lieGroupElement;
    this.lagrangeQuadratic = lagrangeQuadratic;
    this.diff = diff;
    // ---
    clothoidIntegral = ErfClothoidIntegral.interp(lagrangeQuadratic);
    this.length = Norm._2.of(diff).divide(clothoidIntegral.one().abs());
    this.da = ArcTan2D.of(diff);
  }

  @Override // from Clothoid
  public final Scalar length() {
    return length;
  }

  @Override // from Clothoid
  public final LagrangeQuadraticD curvature() {
    return lagrangeQuadratic.derivative(length);
  }

  @Override
  public final Tensor apply(Scalar t) {
    Tensor xya = StaticHelper.prod(clothoidIntegral.normalized(t), diff).append(lagrangeQuadratic.apply(t).add(da));
    return lieGroupElement.combine(xya);
  }
}
