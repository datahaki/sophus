// code by ureif
package ch.alpine.sophus.crv.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;

/** The input parameters b0, b1 are real numbers and represent angles.
 * The return value tilde f(b0, b1) is also a real number.
 * 
 * The approximation quality is very good for |b0|, |b1| <= pi/2
 * 
 * Reference: U. Reif slide 9/32 */
public enum OriginalApproximation implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar _68 = RealScalar.of(68.0);
  private static final Scalar _46 = RealScalar.of(46.0);
  private static final Scalar _1_4 = RealScalar.of(0.25);

  @Override
  public Scalar apply(Scalar b0, Scalar b1) {
    Scalar f1 = b0.multiply(b0).add(b1.multiply(b1)).divide(_68);
    Scalar f2 = b0.multiply(b1).divide(_46);
    Scalar f3 = _1_4;
    return b0.add(b1).multiply(f1.subtract(f2).subtract(f3));
  }
}
