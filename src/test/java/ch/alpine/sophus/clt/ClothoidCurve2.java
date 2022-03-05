// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.pow.Sqrt;

/** 2-point Gauss Legendre quadrature on interval [0, 1] */
/* package */ class ClothoidCurve2 extends ClothoidCurve {
  private static final Scalar HALF = RealScalar.of(0.5);
  private static final Tensor X = Tensors.vector(-1, 1) //
      .multiply(Sqrt.FUNCTION.apply(RationalScalar.of(1, 3))) //
      .map(RealScalar.ONE::add) //
      .divide(RealScalar.of(2));
  private static final Scalar X0 = X.Get(0);
  private static final Scalar X1 = X.Get(1);

  // ---
  public ClothoidCurve2(Tensor p, Tensor q) {
    super(p, q);
  }

  @Override // from ClothoidCurve
  protected Scalar il(Scalar t) {
    // return Total.ofVector(X.multiply(t).map(clothoidQuadratic::exp_i)).multiply(HALF).multiply(t);
    Scalar v0 = exp_i(X0.multiply(t));
    Scalar v1 = exp_i(X1.multiply(t));
    return v0.add(v1).multiply(HALF).multiply(t);
  }

  @Override // from ClothoidCurve
  protected Scalar ir(Scalar t) {
    Scalar _1_t = _1.subtract(t);
    // return Total.ofVector(X.multiply(_1_t).map(t::add).map(clothoidQuadratic::exp_i)).multiply(HALF).multiply(_1_t);
    Scalar v0 = exp_i(X0.multiply(_1_t).add(t));
    Scalar v1 = exp_i(X1.multiply(_1_t).add(t));
    return v0.add(v1).multiply(HALF).multiply(_1_t);
  }
}
