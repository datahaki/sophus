// code by jph
package ch.alpine.sophus.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;

/** function is odd in s1
 * function is even in s2
 * function is odd in s2 */
/* package */ enum MidpointTangentOrder0 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-0.5);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    return F10.multiply(s1);
  }
}
