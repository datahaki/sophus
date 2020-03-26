// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
public abstract class ClothoidImpl implements Clothoid {
  protected final LagrangeQuadratic lagrangeQuadratic;
  protected final ClothoidIntegral clothoidIntegral;
  private final Scalar length;
  private final Scalar da;

  /** @param lagrangeQuadratic
   * @param dxy non-negative
   * @param da angle at parameter 0 */
  public ClothoidImpl(LagrangeQuadratic lagrangeQuadratic, Scalar dxy, Scalar da) {
    this.lagrangeQuadratic = lagrangeQuadratic;
    clothoidIntegral = ErfClothoidIntegral.interp(lagrangeQuadratic);
    this.length = dxy.divide(clothoidIntegral.one().abs());
    this.da = da;
  }

  @Override // from Clothoid
  public final Scalar length() {
    return length;
  }

  @Override // from Clothoid
  public final LagrangeQuadraticD curvature() {
    return lagrangeQuadratic.derivative(length);
  }

  @Override // from Clothoid
  public final Scalar angle(Scalar t) {
    return lagrangeQuadratic.apply(t).add(da);
  }
}
