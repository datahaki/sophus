// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** function is odd in s1
 * function is even in s2
 * function is odd in s2 */
public enum MidpointTangentOrder0 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-0.5);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    return F10.multiply(s1);
  }
}
