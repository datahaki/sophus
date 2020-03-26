// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

/* package */ class OriginClothoid extends ClothoidImpl {
  /** @param q vector of the form {qx, qy, qa} */
  public static Clothoid of(Tensor q) {
    Tensor diff = q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    Scalar qa = q.Get(2);
    Scalar b0 = da.negate(); // normal form T0 == b0
    Scalar b1 = qa.subtract(da); // normal form T1 == b1
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new OriginClothoid(LagrangeQuadratic.interp(b0, bm, b1), diff, da);
  }

  /***************************************************/
  private final Tensor diff;

  private OriginClothoid(LagrangeQuadratic lagrangeQuadratic, Tensor diff, Scalar da) {
    super(lagrangeQuadratic, Norm._2.ofVector(diff), da);
    this.diff = diff;
  }

  @Override
  public Tensor apply(Scalar t) {
    Tensor sxy = StaticHelper.prod(clothoidIntegral.normalized(t), diff);
    return sxy.append(angle(t));
  }
}
