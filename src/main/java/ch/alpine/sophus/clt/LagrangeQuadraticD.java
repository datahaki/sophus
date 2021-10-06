// code by jph
package ch.alpine.sophus.clt;

import java.util.Objects;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Max;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

/** linear polynomial
 * s -> c1 * s + c0 */
public class LagrangeQuadraticD implements ScalarUnaryOperator {
  private final Scalar c0;
  private final Scalar c1;

  /** @param c0
   * @param c1 */
  public LagrangeQuadraticD(Scalar c0, Scalar c1) {
    this.c0 = Objects.requireNonNull(c0);
    this.c1 = Objects.requireNonNull(c1);
  }

  public LagrangeQuadraticD(Number c0, Number c1) {
    this(RealScalar.of(c0), RealScalar.of(c1));
  }

  @Override
  public Scalar apply(Scalar s) {
    return c1.multiply(s).add(c0);
  }

  /** @return value at 0 */
  public Scalar head() {
    return c0;
  }

  /** @return value at 1 */
  public Scalar tail() {
    return c0.add(c1);
  }

  public boolean isZero(Chop chop) {
    return Scalars.isZero(chop.apply(c0)) //
        && Scalars.isZero(chop.apply(c1));
  }

  /** @return maximum of absolute value of the function over the interval [0, 1] */
  public Scalar maxAbs() {
    return Max.of( //
        Abs.FUNCTION.apply(head()), //
        Abs.FUNCTION.apply(tail()));
  }

  public Scalar integralAbs() {
    Scalar sign_head = Sign.FUNCTION.apply(head());
    Scalar sign_tail = Sign.FUNCTION.apply(tail());
    if (sign_head.equals(sign_tail))
      return Abs.FUNCTION.apply(head().add(tail()).multiply(RationalScalar.HALF));
    Scalar abs_head = Abs.FUNCTION.apply(head());
    Scalar abs_tail = Abs.FUNCTION.apply(tail());
    Scalar sum = abs_head.add(abs_tail);
    if (Scalars.isZero(sum))
      return sum;
    Scalar wlo = abs_head.divide(sum);
    Scalar whi = RealScalar.ONE.subtract(wlo);
    Scalar eval0 = LinearInterpolation.of(Tensors.of(abs_tail, abs_head)).At(wlo);
    Scalar eval1 = abs_head.multiply(wlo).add(abs_tail.multiply(whi));
    Tolerance.CHOP.requireClose(eval0, eval1);
    return eval1.multiply(RationalScalar.HALF);
  }
}
