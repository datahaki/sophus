// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Erf;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** for matching end point tangents the real part of the defect should be 0
 * and the imaginary part should be positive */
public class ClothoidTangentDefect implements ScalarUnaryOperator {
  private static final Scalar DIAG = ComplexScalar.of(0.7071067811865476, 0.7071067811865475);

  public static ClothoidTangentDefect of(Number s1, Number s2) {
    return of(RealScalar.of(s1), RealScalar.of(s2));
  }

  public static ClothoidTangentDefect of(Scalar s1, Scalar s2) {
    return new ClothoidTangentDefect(s1, s2);
  }

  /***************************************************/
  private final Scalar s1;
  private final Scalar s2_half;
  private final Scalar s2_half_sqr;

  private ClothoidTangentDefect(Scalar s1, Scalar s2) {
    this.s1 = s1;
    s2_half = s2.multiply(RationalScalar.HALF);
    s2_half_sqr = s2_half.multiply(s2_half);
  }

  @Override
  public Scalar apply(Scalar lam) {
    Scalar factor = DIAG.divide(Sqrt.FUNCTION.apply(lam));
    Scalar exp = Exp.FUNCTION.apply(s2_half_sqr.divide(lam).add(s1).add(lam).multiply(ComplexScalar.I));
    Scalar erf1 = Erf.FUNCTION.apply(s2_half.add(lam).multiply(factor));
    Scalar erf2 = Erf.FUNCTION.apply(s2_half.subtract(lam).multiply(factor));
    return factor.multiply(exp).multiply(erf1.subtract(erf2));
  }

  public Scalar defect(Scalar lam) {
    return Real.FUNCTION.apply(apply(lam));
  }

  public Scalar signum(Scalar lam) {
    return Imag.FUNCTION.apply(apply(lam));
  }
}
