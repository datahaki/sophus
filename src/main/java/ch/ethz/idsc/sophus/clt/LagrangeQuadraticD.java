// code by jph
package ch.ethz.idsc.sophus.clt;

import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Chop;

/** linear polynomial
 * s -> c1 * s + c0 */
public class LagrangeQuadraticD implements ScalarUnaryOperator {
  private static final long serialVersionUID = -4224741445846053217L;

  /** @param c0
   * @param c1
   * @return */
  public static LagrangeQuadraticD of(Scalar c0, Scalar c1) {
    return new LagrangeQuadraticD( //
        Objects.requireNonNull(c0), //
        Objects.requireNonNull(c1));
  }

  /***************************************************/
  private final Scalar c0;
  private final Scalar c1;

  /** @param c0
   * @param c1 */
  /* package */ LagrangeQuadraticD(Scalar c0, Scalar c1) {
    this.c0 = c0;
    this.c1 = c1;
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
