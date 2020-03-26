// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

/** Reference: U. Reif slides */
public class Clothoid implements ScalarTensorFunction {
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
  public Clothoid(Tensor p, Tensor q) {
    pxy = p.extract(0, 2);
    Scalar pa = p.Get(2);
    Tensor qxy = q.extract(0, 2);
    Scalar qa = q.Get(2);
    // ---
    diff = qxy.subtract(pxy);
    da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    b0 = So2.MOD.apply(pa.subtract(da)); // normal form T0 == b0
    b1 = So2.MOD.apply(qa.subtract(da)); // normal form T1 == b1
    bm = ClothoidApproximation.f(b0, b1);
    lagrangeQuadratic = new LagrangeQuadratic(b0, bm, b1);
    clothoidIntegral = ErfClothoidIntegral.interp(lagrangeQuadratic);
    length = Norm._2.ofVector(diff).divide(clothoidIntegral.one().abs());
  }

  /** maps to SE(2) or SE(2) Covering */
  @Override
  public Tensor apply(Scalar t) {
    Scalar z = clothoidIntegral.normalized(t);
    return pxy.add(prod(z, diff)) //
        .append(da.add(lagrangeQuadratic.apply(t)));
  }

  /** @return non-negative approximate length */
  public Scalar length() {
    return length;
  }

  /** @return function that evaluates curvature at given parameter */
  public LagrangeQuadraticD curvature() {
    return new LagrangeQuadraticD(b0.divide(length), bm.divide(length), b1.divide(length));
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
