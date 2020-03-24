// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Arg;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

/* package */ class OriginClothoid implements ScalarTensorFunction {
  private final Tensor qxy;
  private final Scalar qp;
  private final Scalar qxy_arg;
  private final Scalar b0;
  private final Scalar b1;
  private final Scalar bm;
  private final LagrangeQuadratic lagrangeQuadratic;
  private final ClothoidIntegral clothoidIntegral;

  /** @param q vector of the form {qx, qy, qa} */
  public OriginClothoid(Tensor q) {
    qxy = q.extract(0, 2);
    // ---
    // TODO choice: the computation of b0 and b1 is not canonic
    qxy_arg = ArcTan2D.of(qxy); // special case when diff == {0, 0}
    qp = PolarScalar.of( //
        Hypot.of(qxy.Get(0), qxy.Get(1)), //
        qxy_arg);
    Scalar qangle = q.Get(2);
    b0 = qxy_arg.negate(); // normal form T0 == b0
    b1 = qangle.subtract(qxy_arg); // normal form T1 == b1
    bm = ClothoidApproximation.f(b0, b1);
    lagrangeQuadratic = new LagrangeQuadratic(b0, bm, b1);
    clothoidIntegral = ErfClothoidIntegral.interp(lagrangeQuadratic);
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
        qxy_arg.add(lagrangeQuadratic.apply(t)));
  }
}
