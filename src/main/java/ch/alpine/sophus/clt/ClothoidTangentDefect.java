// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Complex;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Im;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.erf.Erf;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.pow.Sqrt;

/** for matching end point tangents the real part of the defect should be 0
 * and the imaginary part should be positive */
public class ClothoidTangentDefect implements ScalarUnaryOperator {
  private static final Scalar DIAG = Complex.unit(Pi.QUARTER);
  private static final Scalar EPS = RealScalar.of(1e-10);

  public static ClothoidTangentDefect of(Scalar s1, Scalar s2) {
    return new ClothoidTangentDefect(s1, s2);
  }

  public static ClothoidTangentDefect of(Number s1, Number s2) {
    return of(RealScalar.of(s1), RealScalar.of(s2));
  }

  public static ClothoidTangentDefect of(ClothoidContext clothoidContext) {
    return of(clothoidContext.s1(), clothoidContext.s2());
  }

  // ---
  private final Scalar s1;
  private final Scalar s2_half;
  private final Scalar s2_half_sqr;

  private ClothoidTangentDefect(Scalar s1, Scalar s2) {
    this.s1 = s1;
    s2_half = s2.multiply(Rational.HALF);
    s2_half_sqr = s2_half.multiply(s2_half);
  }

  @Override
  public Scalar apply(Scalar lam) {
    return Tolerance.CHOP.isZero(lam) //
        ? LinearInterpolation.of(Tensors.of(unsafe(lam.subtract(EPS)), unsafe(lam.add(EPS)))).At(Rational.HALF)
        : unsafe(lam);
  }

  private Scalar unsafe(Scalar lam) {
    Scalar factor = DIAG.divide(Sqrt.FUNCTION.apply(lam));
    Scalar exp = Exp.FUNCTION.apply(s2_half_sqr.divide(lam).add(s1).add(lam).multiply(Complex.I));
    Scalar erf1 = Erf.FUNCTION.apply(s2_half.add(lam).multiply(factor));
    Scalar erf2 = Erf.FUNCTION.apply(s2_half.subtract(lam).multiply(factor));
    return factor.multiply(exp).multiply(erf1.subtract(erf2));
  }

  public Scalar defect(Scalar lam) {
    return Re.FUNCTION.apply(apply(lam));
  }

  public Scalar signum(Scalar lam) {
    return Im.FUNCTION.apply(apply(lam));
  }
}
