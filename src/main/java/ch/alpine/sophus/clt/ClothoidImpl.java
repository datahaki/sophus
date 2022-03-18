// code by ureif
package ch.alpine.sophus.clt;

import java.util.Objects;

import ch.alpine.sophus.clt.par.ClothoidIntegral;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Imag;
import ch.alpine.tensor.sca.Real;

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
  public ClothoidImpl(LieGroupElement lieGroupElement, LagrangeQuadratic lagrangeQuadratic, ClothoidIntegral clothoidIntegral, Tensor diff) {
    this.lieGroupElement = Objects.requireNonNull(lieGroupElement);
    this.lagrangeQuadratic = lagrangeQuadratic;
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
    return lieGroupElement.combine(prod(clothoidIntegral.normalized(t), diff).append(addAngle(t)));
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

  /** complex multiplication between z and vector[0]+i*vector[1]
   * 
   * @param z
   * @param vector of length 2 with entries that may be {@link Quantity}
   * @return vector of length 2 with real entries corresponding to real and imag of result */
  @PackageTestAccess
  static Tensor prod(Scalar z, Tensor vector) {
    Scalar zr = Real.FUNCTION.apply(z);
    Scalar zi = Imag.FUNCTION.apply(z);
    Scalar v0 = vector.Get(0);
    Scalar v1 = vector.Get(1);
    return Tensors.of( //
        zr.multiply(v0).subtract(zi.multiply(v1)), //
        zi.multiply(v0).add(zr.multiply(v1)) //
    );
  }
}
