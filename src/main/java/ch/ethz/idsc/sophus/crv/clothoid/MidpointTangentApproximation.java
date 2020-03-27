// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

public class MidpointTangentApproximation implements ScalarBinaryOperator {
  private static final Scalar HALF = RealScalar.of(0.5);
  /** The input parameters b0, b1 are real numbers and represent angles.
   * The return value tilde f(b0, b1) is also a real number.
   * 
   * The approximation quality is very good for |b0|, |b1| <= pi/2
   * 
   * Reference: U. Reif slide 9/32 */
  public static final ScalarBinaryOperator INSTANCE = new MidpointTangentApproximation(MidpointTangentOrder2.INSTANCE);

  /** @param scalarBinaryOperator
   * @return */
  public static ScalarBinaryOperator create(ScalarBinaryOperator scalarBinaryOperator) {
    return new MidpointTangentApproximation(Objects.requireNonNull(scalarBinaryOperator));
  }

  /***************************************************/
  private final ScalarBinaryOperator scalarBinaryOperator;

  private MidpointTangentApproximation(ScalarBinaryOperator scalarBinaryOperator) {
    this.scalarBinaryOperator = scalarBinaryOperator;
  }

  @Override
  public Scalar apply(Scalar b0, Scalar b1) {
    return scalarBinaryOperator.apply( //
        b0.add(b1).multiply(HALF), //
        b0.subtract(b1).multiply(HALF));
  }
}
