// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
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
public abstract class ClothoidImpl implements Clothoid {
  private final Tensor pxy;
  private final Tensor diff;
  private final Scalar da;
  private final Scalar b0;
  private final Scalar b1;
  private final Scalar bm;
  private final LagrangeQuadratic lagrangeQuadratic;
  private final ClothoidIntegral clothoidIntegral;
  private final Scalar length;

  /** @param p vector of the form {px, py, pa}
   * @param q vector of the form {qx, qy, qa} */
  public ClothoidImpl(Tensor p, Tensor q) {
    pxy = p.extract(0, 2);
    Scalar pa = p.Get(2);
    Tensor qxy = q.extract(0, 2);
    Scalar qa = q.Get(2);
    // ---
    diff = qxy.subtract(pxy);
    da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    b0 = So2.MOD.apply(pa.subtract(da)); // normal form T0 == b0
    b1 = So2.MOD.apply(qa.subtract(da)); // normal form T1 == b1
    bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    lagrangeQuadratic = LagrangeQuadratic.interp(b0, bm, b1);
    clothoidIntegral = ErfClothoidIntegral.interp(lagrangeQuadratic);
    length = Norm._2.ofVector(diff).divide(clothoidIntegral.one().abs());
  }

  @Override
  public Scalar length() {
    return length;
  }

  @Override
  public LagrangeQuadraticD curvature() {
    return lagrangeQuadratic.derivative(length);
  }
}
