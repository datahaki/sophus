// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

/** maps to SE(2)
 * 
 * For parameter 0, the curve evaluates to p.
 * For parameter 1, the curve evaluates to q.
 * 
 * Reference: U. Reif slides */
public class Se2Clothoid extends ClothoidImpl {
  /** @param p vector of the form {px, py, pa}
   * @param q vector of the form {qx, qy, qa} */
  public static Clothoid of(Tensor p, Tensor q) {
    Tensor pxy = p.extract(0, 2);
    Scalar pa = p.Get(2);
    Tensor qxy = q.extract(0, 2);
    Scalar qa = q.Get(2);
    // ---
    Tensor diff = qxy.subtract(pxy);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    Scalar b0 = So2.MOD.apply(pa.subtract(da)); // normal form T0 == b0
    Scalar b1 = So2.MOD.apply(qa.subtract(da)); // normal form T1 == b1
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new Se2Clothoid(LagrangeQuadratic.interp(b0, bm, b1), diff, pxy, da);
  }

  /***************************************************/
  private final Tensor diff;
  private final Tensor pxy;

  private Se2Clothoid(LagrangeQuadratic lagrangeQuadratic, Tensor diff, Tensor pxy, Scalar da) {
    super(lagrangeQuadratic, Norm._2.ofVector(diff), da);
    this.diff = diff;
    this.pxy = pxy;
  }

  @Override
  public Tensor apply(Scalar t) {
    return pxy.add(prod(clothoidIntegral.normalized(t), diff)).append(angle(t));
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
