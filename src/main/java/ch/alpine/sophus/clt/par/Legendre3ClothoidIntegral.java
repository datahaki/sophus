// code by ureif
// code by jph
package ch.alpine.sophus.clt.par;

import java.io.Serializable;

import ch.alpine.sophus.clt.LagrangeQuadratic;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Sqrt;

/** 3-point Gauss Legendre quadrature on interval [0, 1] */
/* package */ class Legendre3ClothoidIntegral implements ClothoidPartial, ClothoidIntegral, Serializable {
  private static final Scalar _1 = RealScalar.of(1.0);
  private static final Tensor W = Tensors.vector(5, 8, 5).divide(RealScalar.of(18.0));
  private static final Tensor X = Tensors.vector(-1, 0, 1) //
      .multiply(Sqrt.FUNCTION.apply(RationalScalar.of(3, 5))) //
      .map(RealScalar.ONE::add) //
      .divide(RealScalar.of(2));
  private static final Scalar X0 = X.Get(0);
  private static final Scalar X1 = X.Get(1);
  private static final Scalar X2 = X.Get(2);
  private static final Scalar W0 = W.Get(0);
  private static final Scalar W1 = W.Get(1);
  // ---
  private final ScalarUnaryOperator scalarUnaryOperator;

  /** @param scalarUnaryOperator typically a quadratic polynomial
   * 
   * @see LagrangeQuadratic */
  public Legendre3ClothoidIntegral(ScalarUnaryOperator scalarUnaryOperator) {
    this.scalarUnaryOperator = scalarUnaryOperator;
  }

  @Override // from ClothoidPartial
  public Scalar il(Scalar t) {
    Scalar v0 = exp_i(X0.multiply(t));
    Scalar v1 = exp_i(X1.multiply(t));
    Scalar v2 = exp_i(X2.multiply(t));
    return v0.add(v2).multiply(W0).add(v1.multiply(W1)).multiply(t);
  }

  private Scalar ir(Scalar t) {
    Scalar _1_t = _1.subtract(t);
    Scalar v0 = exp_i(X0.multiply(_1_t).add(t));
    Scalar v1 = exp_i(X1.multiply(_1_t).add(t));
    Scalar v2 = exp_i(X2.multiply(_1_t).add(t));
    return v0.add(v2).multiply(W0).add(v1.multiply(W1)).multiply(_1_t);
  }

  @Override // from ClothoidIntegral
  public Scalar normalized(Scalar t) {
    Scalar il = il(t);
    Scalar ir = ir(t);
    /** ratio z enforces interpolation of terminal points
     * t == 0 -> (0, 0)
     * t == 1 -> (1, 0) */
    return il.divide(il.add(ir));
  }

  @Override // from ClothoidIntegral
  public Scalar one() {
    Scalar v0 = exp_i(X0);
    Scalar v1 = exp_i(X1);
    Scalar v2 = exp_i(X2);
    return v0.add(v2).multiply(W0).add(v1.multiply(W1));
  }

  private Scalar exp_i(Scalar s) {
    return ComplexScalar.unit(scalarUnaryOperator.apply(s));
  }
}
