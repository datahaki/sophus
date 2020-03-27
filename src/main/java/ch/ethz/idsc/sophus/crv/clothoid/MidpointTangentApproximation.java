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
/* package */ enum MidpointTangentApproximation implements BinaryOperator<Scalar> {
  INSTANCE;

  private static final Scalar HALF = RealScalar.of(0.5);

  @Override
  public Scalar apply(Scalar b0, Scalar b1) {
    return MidpointTangentOrder2.INSTANCE.apply( //
        b0.add(b1).multiply(HALF), //
        b0.subtract(b1).multiply(HALF));
  }
}
