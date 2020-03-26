// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** The input parameters b0, b1 are real numbers and represent angles.
 * The return value tilde f(b0, b1) is also a real number.
 * 
 * The approximation quality is very good for |b0|, |b1| <= pi/2
 * 
 * Reference: U. Reif slide 9/32 */
/* package */ enum OriginalApproximation implements BinaryOperator<Scalar> {
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
