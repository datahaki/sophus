// code by jph
package ch.alpine.sophus.clt.mid;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

/** function is odd in s1
 * function is even in s2
 * 
 * bm == lambda + s1
 * lambda == bm - s1 */
public enum MidpointTangentOrder2 implements BinaryOperator<Scalar> {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-1.5);
  private static final Scalar F12 = RealScalar.of(+6.0 / 391);
  private static final Scalar F32 = RealScalar.of(+40.0 / 391);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    Scalar p20 = s1.multiply(s1); // s1^2
    Scalar p22 = s2.multiply(s2); // s2^2
    return F10.add(F12.multiply(p20)).add(F32.multiply(p22)).multiply(s1);
  }
}
