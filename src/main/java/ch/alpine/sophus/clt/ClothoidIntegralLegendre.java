// code by ureif
// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Complex;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.sca.pow.Sqrt;

/** 3-point Gauss Legendre quadrature on interval [0, 1]
 * 
 * @param lagrangeQuadratic typically a quadratic polynomial
 * 
 * @see LagrangeQuadratic */
record ClothoidIntegralLegendre(LagrangeQuadratic lagrangeQuadratic) implements ScalarUnaryOperator, ClothoidIntegral {
  private static final Scalar _1 = RealScalar.of(1.0);
  private static final Tensor W = Tensors.vector(5, 8, 5).divide(RealScalar.of(18.0));
  private static final Tensor X = Tensors.vector(-1, 0, 1) //
      .multiply(Sqrt.FUNCTION.apply(Rational.of(3, 5))) //
      .maps(RealScalar.ONE::add) //
      .divide(RealScalar.TWO);
  private static final Scalar X0 = X.Get(0);
  private static final Scalar X1 = X.Get(1);
  private static final Scalar X2 = X.Get(2);
  private static final Scalar W0 = W.Get(0);
  private static final Scalar W1 = W.Get(1);

  @Override // from ClothoidPartial
  public Scalar apply(Scalar t) {
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
    Scalar il = apply(t);
    Scalar ir = ir(t);
    /* ratio z enforces interpolation of terminal points
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
    return Complex.unit(lagrangeQuadratic.apply(s));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("ClothoidIntegralLegendre", one());
  }
}
