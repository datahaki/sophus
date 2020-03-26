// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

/** maps to SE(2) or SE(2) Covering
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
public class Se2Clothoid implements Clothoid {
  static final InterpolatingPolynomial INTERPOLATING_POLYNOMIAL = //
      InterpolatingPolynomial.of(Tensors.vector(0.0, 0.5, 1.0));
  // ---
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
  public Se2Clothoid(Tensor p, Tensor q) {
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
  public Tensor apply(Scalar t) {
    return pxy.add(prod(clothoidIntegral.normalized(t), diff)).append(angle(t));
  }

  /** @param t
   * @return */
  @Override
  public Scalar angle(Scalar t) {
    return da.add(lagrangeQuadratic.apply(t));
  }

  /** @return non-negative approximate length */
  @Override
  public Scalar length() {
    return length;
  }

  /** Example:
   * parameter 0 returns curvature of clothoid at p
   * parameter 1 returns curvature of clothoid at q
   * 
   * @return function that evaluates curvature at given parameter */
  @Override
  public LagrangeQuadraticD curvature() {
    return lagrangeQuadratic.derivative(length);
    // return LagrangeQuadraticD.interp(b0.divide(length), bm.divide(length), b1.divide(length));
  }

  /** complex multiplication between z and vector[0]+i*vector[1]
   * 
   * @param z
   * @param vector of length 2 with entries that may be {@link Quantity}
   * @return vector of length 2 with real entries corresponding to real and imag of result */
  /* package */ static Tensor prod(Scalar z, Tensor vector) {
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
