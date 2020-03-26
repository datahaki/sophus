// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class LagrangeQuadraticD implements ScalarUnaryOperator, HeadTailInterface {
  private final Scalar c0;
  private final Scalar c1;

  /** @param c0
   * @param c1 */
  public LagrangeQuadraticD(Scalar c0, Scalar c1) {
    this.c0 = c0;
    this.c1 = c1;
  }

  @Override
  public Scalar apply(Scalar s) {
    return c1.multiply(s).add(c0);
  }

  @Override // from HeadTailInterface
  public Scalar head() {
    return c0;
  }

  @Override // from HeadTailInterface
  public Scalar tail() {
    return c0.add(c1);
  }
}
