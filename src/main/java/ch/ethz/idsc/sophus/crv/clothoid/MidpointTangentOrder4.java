// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** function is odd in s1
 * function is even in s2 */
/* package */ enum MidpointTangentOrder4 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar S1 = RealScalar.of(-0.25);
  private static final Scalar S1_3 = RealScalar.of(-0.008519475520375132);
  private static final Scalar S1_5 = RealScalar.of(0.00042609940121572323);
  private static final Scalar S1_3_S2_2 = RealScalar.of(-0.0017491104068500516);
  private static final Scalar S1_S2_2 = RealScalar.of(0.05279598754256225);
  private static final Scalar S1_S2_4 = RealScalar.of(0.0003862364654546811);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    Scalar s1_2 = s1.multiply(s1); // s1^2
    Scalar s2_2 = s2.multiply(s2); // s2^2
    Scalar s1_2_s1_2 = s1_2.multiply(s2_2); // s1^2 s2^2
    Scalar s1_4 = s1_2.multiply(s1_2); // s1^4
    Scalar s2_4 = s2_2.multiply(s2_2); // s2^4
    return S1 //
        .add(S1_3.multiply(s1_2)) //
        .add(S1_5.multiply(s1_4)) //
        .add(S1_3_S2_2.multiply(s1_2_s1_2)) //
        .add(S1_S2_2.multiply(s2_2)) //
        .add(S1_S2_4.multiply(s2_4)).multiply(s1);
  }
}
