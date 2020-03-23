// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Arg;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ class OriginClothoid implements Serializable {
  private final Tensor qxy;
  private final Scalar qp;
  private final Scalar qxy_arg;
  private final Scalar b0;
  private final Scalar b1;
  private final Scalar bm;

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
  }

  public Curve legendre3() {
    ScalarUnaryOperator scalarUnaryOperator = //
        Clothoid.INTERPOLATING_POLYNOMIAL.scalarUnaryOperator(Tensors.of(b0, bm, b1));
    return new Curve(scalarUnaryOperator, new Legendre3ClothoidIntegral(scalarUnaryOperator));
  }

  public Curve erf() {
    return new Curve( //
        Clothoid.INTERPOLATING_POLYNOMIAL.scalarUnaryOperator(Tensors.of(b0, bm, b1)), //
        ErfClothoidIntegral.interp(b0, bm, b1));
  }

  public final class Curve implements ScalarTensorFunction {
    private final ScalarUnaryOperator scalarUnaryOperator;
    private final ClothoidIntegral clothoidIntegral;

    private Curve(ScalarUnaryOperator scalarUnaryOperator, ClothoidIntegral clothoidIntegral) {
      this.scalarUnaryOperator = scalarUnaryOperator;
      this.clothoidIntegral = clothoidIntegral;
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
          qxy_arg.add(scalarUnaryOperator.apply(t)));
    }
  }
}
