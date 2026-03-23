// code by ureif
package ch.alpine.sophus.clt;

import java.io.Serializable;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.erf.Erfi;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.ply.Polynomial;
import ch.alpine.tensor.sca.pow.Sqrt;

/** The integral of exp i*clothoidQuadratic as suggested in U. Reif slides has the
 * property, that the values of the polynomial correspond to the tangent angle.
 * 
 * approximate integration of Exp[ i*Polynomial({c0, c1, c2}) ] on [0, t]
 * 
 * class is public for inspection and plotting purposes */
public class ClothoidIntegralAnalytic implements ClothoidIntegral, Serializable {
  public static ScalarUnaryOperator clothoidPartial(Polynomial polynomial) {
    Tensor coeffs = polynomial.coeffs();
    Scalar c0 = coeffs.Get(0);
    Scalar c1 = coeffs.Get(1);
    if (coeffs.length() < 3 || //
        Tolerance.CHOP.isZero(coeffs.Get(2)))
      return Tolerance.CHOP.isZero(c1) //
          ? new Degree0(c0)
          : new Degree1(c0, c1);
    return new Degree2(coeffs);
  }

  /** @param lagrangeQuadratic
   * @return */
  public static ScalarUnaryOperator of(LagrangeQuadratic lagrangeQuadratic) {
    return clothoidPartial(lagrangeQuadratic.polynomial());
  }

  static ScalarUnaryOperator of(Number c0, Number c1, Number c2) {
    return clothoidPartial(Polynomial.of(Tensors.vector(c0, c1, c2)));
  }

  // ---
  private final LagrangeQuadratic lagrangeQuadratic;
  private final ScalarUnaryOperator clothoidPartial;
  private final Scalar one;

  public ClothoidIntegralAnalytic(LagrangeQuadratic lagrangeQuadratic) {
    this.lagrangeQuadratic = lagrangeQuadratic;
    clothoidPartial = of(lagrangeQuadratic);
    one = clothoidPartial.apply(RealScalar.ONE);
  }

  @Override
  public LagrangeQuadratic lagrangeQuadratic() {
    return lagrangeQuadratic;
  }

  @Override // from ClothoidIntegral
  public Scalar normalized(Scalar t) {
    return clothoidPartial.apply(t).divide(one);
  }

  @Override // from ClothoidIntegral
  public Scalar one() {
    return one;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("ClothoidIntegralAnalytic", one());
  }

  /** exact solution taken from
   * https://www.wolframalpha.com/input?i=Integral%5BExp%5Bi+c0%5D%2C%7Bx%2C0%2Ct%7D%5D */
  private static class Degree0 implements ScalarUnaryOperator {
    private final Scalar factor;

    public Degree0(Scalar c0) {
      factor = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
    }

    @Override // from ClothoidPartial
    public Scalar apply(Scalar t) {
      return t.multiply(factor);
    }
  }

  /** exact solution taken from
   * https://www.wolframalpha.com/input?i=Integral%5BExp%5Bi+%28c0%2Bc1+x%29%5D%2C%7Bx%2C0%2Ct%7D%5D */
  private static class Degree1 implements ScalarUnaryOperator {
    private final Scalar c0;
    private final Scalar c1;
    private final Scalar factor;
    private final Scalar ofs;

    public Degree1(Scalar c0, Scalar c1) {
      this.c0 = c0;
      this.c1 = c1;
      factor = ComplexScalar.I.divide(c1);
      ofs = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
    }

    @Override // from ClothoidPartial
    public Scalar apply(Scalar t) {
      Scalar ofs2 = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c1.multiply(t).add(c0)));
      return ofs.subtract(ofs2).multiply(factor);
    }
  }

  /** exact solution taken from
   * https://www.wolframalpha.com */
  private static class Degree2 implements ScalarUnaryOperator {
    private static final Scalar _N1_1_4 = ComplexScalar.unit(Pi.QUARTER);
    private static final Scalar _N1_3_4 = ComplexScalar.unit(Pi._3_4);
    private static final Scalar _1_4 = Rational.of(1, 4);
    // ---
    private final Scalar c1;
    private final Scalar c2;
    private final Scalar f4;
    private final Scalar factor;
    private final Scalar ofs;

    /** @param coeffs {c0, c1, c2} */
    public Degree2(Tensor coeffs) {
      Scalar c0 = coeffs.Get(0);
      this.c1 = coeffs.Get(1);
      this.c2 = coeffs.Get(2);
      Scalar f1 = _N1_3_4;
      Scalar f2 = Exp.FUNCTION.apply(c0.subtract(_1_4.multiply(c1).multiply(c1).divide(c2)).multiply(ComplexScalar.I));
      Scalar f3 = Sqrt.FUNCTION.apply(Pi.VALUE);
      f4 = Rational.HALF.divide(Sqrt.FUNCTION.apply(c2));
      factor = Times.of(f1, f2, f3, f4);
      ofs = Erfi.FUNCTION.apply(_N1_1_4.multiply(c1).multiply(f4));
    }

    @Override // from ClothoidPartial
    public Scalar apply(Scalar t) {
      Scalar c2t = c2.multiply(t);
      Scalar ofs2 = Erfi.FUNCTION.apply(_N1_1_4.multiply(c1.add(c2t).add(c2t)).multiply(f4));
      return ofs.subtract(ofs2).multiply(factor);
    }
  }
}
