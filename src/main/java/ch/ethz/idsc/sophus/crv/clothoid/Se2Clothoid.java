// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

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
    Tensor sxy = StaticHelper.prod(clothoidIntegral.normalized(t), diff);
    return pxy.add(sxy).append(angle(t));
  }
}
