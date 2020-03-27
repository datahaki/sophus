// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** function is odd in s1
 * function is even in s2 */
public enum MidpointTangentOrder2 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar N1_2 = RealScalar.of(-0.5);
  private static final Scalar S1_1 = RealScalar.of(6.0 / 391);
  private static final Scalar S2_1 = RealScalar.of(40.0 / 391);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    Scalar s1_2 = s1.multiply(s1); // s1^2
    Scalar s2_2 = s2.multiply(s2); // s2^2
    return N1_2.add(S1_1.multiply(s1_2)).add(S2_1.multiply(s2_2)).multiply(s1);
  }
}
