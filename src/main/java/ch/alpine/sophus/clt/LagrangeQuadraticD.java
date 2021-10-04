// code by jph
package ch.alpine.sophus.clt;

import java.util.Objects;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.red.Max;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;

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

  public Scalar absMax() {
    return Max.of( //
        Abs.FUNCTION.apply(head()), //
        Abs.FUNCTION.apply(tail()));
  }
}
