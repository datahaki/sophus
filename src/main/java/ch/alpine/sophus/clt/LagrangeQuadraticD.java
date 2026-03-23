// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.itp.LinearBinaryAverage;
import ch.alpine.tensor.red.Max;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.ply.Polynomial;

/** linear polynomial
 * s -> c1 * s + c0 */
public record LagrangeQuadraticD(Polynomial polynomial) implements ScalarUnaryOperator {
  /** @param c0
   * @param c1 */
  @PackageTestAccess
  static LagrangeQuadraticD of(Scalar c0, Scalar c1) {
    return new LagrangeQuadraticD(Polynomial.of(Tensors.of(c0, c1)));
  }

  @PackageTestAccess
  static LagrangeQuadraticD of(Number c0, Number c1) {
    return of(RealScalar.of(c0), RealScalar.of(c1));
  }

  @Override
  public Scalar apply(Scalar s) {
    return polynomial.apply(s);
  }

  /** @return value at 0 */
  public Scalar head() {
    return apply(RealScalar.ZERO);
  }

  /** @return value at 1 */
  public Scalar tail() {
    return apply(RealScalar.ONE);
  }

  public boolean isZero(Chop chop) {
    return chop.allZero(polynomial.coeffs());
  }

  /** @return maximum of absolute value of the function over the interval [0, 1] */
  public Scalar maxAbs() {
    return Max.of( //
        Abs.FUNCTION.apply(head()), //
        Abs.FUNCTION.apply(tail()));
  }

  /** @return integral of absolute value of function over the interval [0, 1] */
  public Scalar integralAbs() {
    Scalar sign_head = Sign.FUNCTION.apply(head());
    Scalar sign_tail = Sign.FUNCTION.apply(tail());
    if (sign_head.equals(sign_tail))
      return Abs.FUNCTION.apply(head().add(tail()).multiply(Rational.HALF));
    Scalar abs_head = Abs.FUNCTION.apply(head());
    Scalar abs_tail = Abs.FUNCTION.apply(tail());
    Scalar sum = abs_head.add(abs_tail);
    return Scalars.isZero(sum) //
        ? sum
        : (Scalar) LinearBinaryAverage.INSTANCE.split(abs_head, abs_tail, abs_tail.divide(sum)) //
            .multiply(Rational.HALF);
  }
}
