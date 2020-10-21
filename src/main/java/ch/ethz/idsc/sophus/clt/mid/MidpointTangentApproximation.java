// code by ureif
package ch.ethz.idsc.sophus.clt.mid;

import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Sign;

public class MidpointTangentApproximation implements BinaryOperator<Scalar> {
  private static final Scalar HALF = RealScalar.of(0.5);
  /** The input parameters b0, b1 are real numbers and represent angles.
   * The return value tilde f(b0, b1) is also a real number.
   * 
   * The approximation quality is very good for |b0|, |b1| <= pi/2
   * 
   * Reference: U. Reif slide 9/32 */
  public static final BinaryOperator<Scalar> ORDER2 = new MidpointTangentApproximation(MidpointTangentOrder2.INSTANCE);
  public static final BinaryOperator<Scalar> ORDER4 = new MidpointTangentApproximation(MidpointTangentOrder4.INSTANCE);
  public static final BinaryOperator<Scalar> LAYERS = new MidpointTangentApproximation(MidpointTangentLayers.INSTANCE);

  /** @param binaryOperator
   * @return */
  public static BinaryOperator<Scalar> create(BinaryOperator<Scalar> binaryOperator) {
    return new MidpointTangentApproximation(Objects.requireNonNull(binaryOperator));
  }

  /***************************************************/
  private final BinaryOperator<Scalar> binaryOperator;

  private MidpointTangentApproximation(BinaryOperator<Scalar> binaryOperator) {
    this.binaryOperator = binaryOperator;
  }

  @Override
  public Scalar apply(Scalar b0, Scalar b1) {
    final Scalar s1 = b0.add(b1).multiply(HALF);
    final Scalar s2 = b0.subtract(b1).multiply(HALF);
    Scalar ns1 = s1;
    Scalar signum = RealScalar.ONE;
    if (Sign.isNegative(s1)) {
      ns1 = ns1.negate();
      signum = signum.negate();
    }
    if (Scalars.lessThan(Pi.VALUE, ns1)) {
      ns1 = Pi.TWO.subtract(ns1);
      signum = signum.negate();
    }
    Scalar ns2 = Abs.FUNCTION.apply(s2);
    return binaryOperator.apply(ns1, ns2).multiply(signum).add(s1);
  }
}
