// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** function is odd in s1
 * function is even in s2
 * function is odd in s2 */
/* package */ enum MidpointTangentOrder0 implements BinaryOperator<Scalar> {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-0.5);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    return F10.multiply(s1);
  }
}
