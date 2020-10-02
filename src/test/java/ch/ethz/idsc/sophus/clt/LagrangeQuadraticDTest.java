// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LagrangeQuadraticDTest extends TestCase {
  private static final Scalar _3 = RealScalar.of(+3);
  private static final Scalar _4 = RealScalar.of(+4);

  /** The Lagrange interpolating polynomial is linear with the following coefficients
   * {-3 b0 + 4 bm - b1, 4 (b0 - 2 bm + b1)}
   * 
   * @param b0
   * @param bm
   * @param b1 */
  public static LagrangeQuadraticD interp(Scalar b0, Scalar bm, Scalar b1) {
    return new LagrangeQuadraticD( //
        bm.multiply(_4).subtract(b1).subtract(b0.multiply(_3)), //
        b0.add(b1).subtract(bm.add(bm)).multiply(_4));
  }

  public void testSimple() {
    Scalar b0 = RealScalar.of(0.7);
    Scalar bm = RealScalar.of(0.3);
    Scalar b1 = RealScalar.of(-0.82);
    Scalar length = RealScalar.of(13.2);
    LagrangeQuadraticD lqd1 = LagrangeQuadratic.interp(b0, bm, b1).derivative(length);
    LagrangeQuadraticD lqd2 = interp(b0.divide(length), bm.divide(length), b1.divide(length));
    Tensor domain = RandomVariate.of(UniformDistribution.unit(), 10);
    Tolerance.CHOP.requireClose(domain.map(lqd1), domain.map(lqd2));
  }

  public void testZero() {
    LagrangeQuadraticD lagrangeQuadraticD = LagrangeQuadraticD.of(RealScalar.of(1e-9), RealScalar.of(1e-10));
    assertTrue(lagrangeQuadraticD.isZero(Chop._08));
  }

  public void testNullFail() {
    AssertFail.of(() -> LagrangeQuadraticD.of(null, RealScalar.ONE));
    AssertFail.of(() -> LagrangeQuadraticD.of(RealScalar.ONE, null));
  }
}
