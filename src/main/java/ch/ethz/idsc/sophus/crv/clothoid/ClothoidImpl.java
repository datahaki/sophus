// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.util.Objects;

import ch.ethz.idsc.sophus.crv.ArcTan2D;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Abs;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
/* package */ final class ClothoidImpl implements Clothoid {
  private final LieGroupElement lieGroupElement;
  private final LagrangeQuadratic lagrangeQuadratic;
  private final Tensor diff;
  private final ClothoidIntegral clothoidIntegral;
  private final Scalar length;
  private final Scalar da;

  /** @param lieGroupElement
   * @param lagrangeQuadratic
   * @param diff vector of length 2 */
  public ClothoidImpl(LieGroupElement lieGroupElement, LagrangeQuadratic lagrangeQuadratic, Tensor diff) {
    this.lieGroupElement = Objects.requireNonNull(lieGroupElement);
    this.lagrangeQuadratic = lagrangeQuadratic;
    this.diff = diff;
    // ---
    clothoidIntegral = ClothoidIntegral.interp(lagrangeQuadratic);
    Scalar one = clothoidIntegral.one(); // ideally should have Im[one] == 0
    Scalar plength = RealScalar.ZERO;
    try {
      plength = Hypot.ofVector(diff).divide(Abs.FUNCTION.apply(one));
    } catch (Exception exception) {
      System.err.println("---");
      System.err.println(diff);
      System.err.println(one);
    }
    this.length = plength;
    this.da = ArcTan2D.of(diff);
  }

  @Override
  public Tensor apply(Scalar t) {
    return lieGroupElement.combine(StaticHelper.prod(clothoidIntegral.normalized(t), diff).append(addAngle(t)));
  }

  @Override // from Clothoid
  public Scalar length() {
    return length;
  }

  @Override // from Clothoid
  public Scalar addAngle(Scalar t) {
    return lagrangeQuadratic.apply(t).add(da);
  }

  @Override // from Clothoid
  public LagrangeQuadraticD curvature() {
    return lagrangeQuadratic.derivative(length);
  }
}
