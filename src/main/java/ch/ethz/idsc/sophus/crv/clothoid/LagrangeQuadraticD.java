// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class LagrangeQuadraticD implements ScalarUnaryOperator {
  private final Scalar c0;
  private final Scalar c1;

  /** @param c0
   * @param c1 */
  public LagrangeQuadraticD(Scalar c0, Scalar c1) {
    this.c0 = c0;
    this.c1 = c1;
  }

  public boolean isZero() {
    return Scalars.isZero(Tolerance.CHOP.apply(c0)) //
        && Scalars.isZero(Tolerance.CHOP.apply(c1));
  }

  @Override
  public Scalar apply(Scalar s) {
    return c1.multiply(s).add(c0);
  }

  public Scalar head() {
    return c0;
  }

  public Scalar tail() {
    return c0.add(c1);
  }

  public boolean equals(LagrangeQuadraticD lagrangeQuadraticD) {
    return false;
  }

  public Scalar absMax() {
    return Max.of( //
        Abs.FUNCTION.apply(head()), //
        Abs.FUNCTION.apply(tail()));
  }
}
