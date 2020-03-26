// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Arg;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

/* package */ class OriginClothoid extends ClothoidImpl {
  /** @param q vector of the form {qx, qy, qa} */
  public static Clothoid of(Tensor q) {
    Tensor diff = q.extract(0, 2); // TODO choice: the computation of b0 and b1 is not canonic
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    Scalar qp = PolarScalar.of( //
        Hypot.of(diff.Get(0), diff.Get(1)), //
        da);
    Scalar qangle = q.Get(2);
    Scalar b0 = da.negate(); // normal form T0 == b0
    Scalar b1 = qangle.subtract(da); // normal form T1 == b1
    Scalar bm = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
    return new OriginClothoid(LagrangeQuadratic.interp(b0, bm, b1), diff, qp, da);
  }

  /***************************************************/
  private final Scalar qp;

  private OriginClothoid(LagrangeQuadratic lagrangeQuadratic, Tensor diff, Scalar qp, Scalar da) {
    super(lagrangeQuadratic, Norm._2.ofVector(diff), da);
    this.qp = qp;
  }

  @Override
  public Tensor apply(Scalar t) {
    Scalar zcomplex = clothoidIntegral.normalized(t);
    PolarScalar z = PolarScalar.of(zcomplex.abs(), Arg.FUNCTION.apply(zcomplex));
    PolarScalar zq = z.multiply(qp);
    // TODO check code below
    return Tensors.of( //
        Real.FUNCTION.apply(zq), //
        Imag.FUNCTION.apply(zq), //
        angle(t));
  }
}
