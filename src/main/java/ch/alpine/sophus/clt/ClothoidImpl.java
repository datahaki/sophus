// code by ureif
package ch.alpine.sophus.clt;

import java.util.Objects;

import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.ReIm;
import ch.alpine.tensor.sca.Abs;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
/* package */ class ClothoidImpl implements Clothoid {
  private final Tensor xya;
  private final LagrangeQuadratic lagrangeQuadratic;
  private final Tensor diff;
  private final ClothoidIntegral clothoidIntegral;
  private final Scalar length;
  private final Scalar da;

  /** @param lieGroupElement
   * @param lagrangeQuadratic
   * @param diff vector of length 2 */
  public ClothoidImpl(Tensor xya, ClothoidIntegral clothoidIntegral, Tensor diff) {
    this.xya = Objects.requireNonNull(xya);
    this.lagrangeQuadratic = clothoidIntegral.lagrangeQuadratic();
    this.diff = diff;
    this.clothoidIntegral = clothoidIntegral;
    Scalar one = clothoidIntegral.one(); // ideally should have Im[one] == 0
    Scalar plength = RealScalar.ZERO;
    try {
      plength = Vector2Norm.of(diff).divide(Abs.FUNCTION.apply(one));
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
    Tensor other = ReIm.of(clothoidIntegral.normalized(t)).rotate(diff).append(addAngle(t));
    return Se2CoveringGroup.INSTANCE.combine(xya, other);
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

  @Override
  public String toString() {
    return MathematicaFormat.concise("Clothoid", xya, diff, clothoidIntegral);
  }
}
