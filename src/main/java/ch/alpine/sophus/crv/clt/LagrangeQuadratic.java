// code by ureif
package ch.alpine.sophus.crv.clt;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.FiniteScalarQ;
import ch.alpine.tensor.sca.ply.InterpolatingPolynomial;

/** Reference: U. Reif slide 8/32
 * 
 * quadratic polynomial that interpolates given values at parameters 0, 1/2, 1:
 * <pre>
 * p[0/2] == b0
 * p[1/2] == bm
 * p[2/2] == b1
 * </pre>
 * 
 * <p>In Mathematica, the coefficients can be obtained via
 * <pre>
 * InterpolatingPolynomial[{{0, b0}, {1/2, bm}, {1, b1}}, x]
 * == b0 + (-3 b0 + 4 bm - b1) x + 2 (b0 + b1 - 2 bm) x^2
 * </pre>
 * 
 * @see InterpolatingPolynomial */
public class LagrangeQuadratic implements ScalarUnaryOperator {
  private static final Scalar _3 = RealScalar.of(+3.0);

  /** The Lagrange interpolating polynomial has the following coefficients
   * {b0, -3 b0 - b1 + 4 bm, 2 (b0 + b1 - 2 bm)}
   * 
   * <p>Typically, the input parameters b0, bm, b1 are real numbers and
   * represent angles.
   * 
   * @param b0
   * @param bm
   * @param b1 */
  public static LagrangeQuadratic interp(Scalar b0, Scalar bm, Scalar b1) {
    Scalar bm2 = bm.add(bm);
    Scalar t2 = b0.add(b1).subtract(bm2);
    return new LagrangeQuadratic( //
        b0, //
        bm2.add(bm2).subtract(b0.multiply(_3).add(b1)), //
        t2.add(t2));
  }

  // ---
  private final Scalar c0;
  private final Scalar c1;
  private final Scalar c2;

  private LagrangeQuadratic(Scalar c0, Scalar c1, Scalar c2) {
    this.c0 = c0;
    this.c1 = c1;
    this.c2 = c2;
  }

  @Override
  public Scalar apply(Scalar s) {
    return c2.multiply(s).add(c1).multiply(s).add(c0);
  }

  /** @param length
   * @return linear polynomial */
  public LagrangeQuadraticD derivative(Scalar length) {
    if (Scalars.isZero(length))
      return new LagrangeQuadraticD(c1.zero(), c2.zero());
    Scalar d_c0 = c1.divide(length);
    Scalar d_c1 = c2.add(c2).divide(length);
    return FiniteScalarQ.of(d_c0) //
        && FiniteScalarQ.of(d_c1) //
            ? new LagrangeQuadraticD(d_c0, d_c1)
            : new LagrangeQuadraticD(c1.zero(), c2.zero());
  }

  public Scalar c0() {
    return c0;
  }

  public Scalar c1() {
    return c1;
  }

  public Scalar c2() {
    return c2;
  }
}
