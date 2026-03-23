// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class LagrangeQuadraticDTest {
  private static final Scalar _3 = RealScalar.of(+3);
  private static final Scalar _4 = RealScalar.of(+4);

  /** The Lagrange interpolating polynomial is linear with the following coefficients
   * {-3 b0 + 4 bm - b1, 4 (b0 - 2 bm + b1)}
   * 
   * @param b0
   * @param bm
   * @param b1 */
  public static LagrangeQuadraticD interp(Scalar b0, Scalar bm, Scalar b1) {
    return LagrangeQuadraticD.of( //
        bm.multiply(_4).subtract(b1).subtract(b0.multiply(_3)), //
        b0.add(b1).subtract(bm.add(bm)).multiply(_4));
  }

  @Test
  void testSimple() {
    Scalar b0 = RealScalar.of(0.7);
    Scalar bm = RealScalar.of(0.3);
    Scalar b1 = RealScalar.of(-0.82);
    Scalar length = RealScalar.of(13.2);
    LagrangeQuadraticD lqd1 = LagrangeQuadratic.interp(b0, bm, b1).derivative(length);
    LagrangeQuadraticD lqd2 = interp(b0.divide(length), bm.divide(length), b1.divide(length));
    Tensor domain = RandomVariate.of(UniformDistribution.unit(), 10);
    Tolerance.CHOP.requireClose(domain.maps(lqd1), domain.maps(lqd2));
  }

  @Test
  void testZero() throws ClassNotFoundException, IOException {
    LagrangeQuadraticD lagrangeQuadraticD = Serialization.copy(LagrangeQuadraticD.of(1e-9, 1e-10));
    assertTrue(lagrangeQuadraticD.isZero(Chop._08));
  }

  @Test
  void testIntegralAbs() {
    assertEquals(LagrangeQuadraticD.of(+2, +2).integralAbs(), RealScalar.of(3));
    assertEquals(LagrangeQuadraticD.of(-2, -2).integralAbs(), RealScalar.of(3));
    assertEquals(LagrangeQuadraticD.of(-1, +2).integralAbs(), Rational.HALF);
    assertEquals(LagrangeQuadraticD.of(+1, -2).integralAbs(), Rational.HALF);
    assertEquals(LagrangeQuadraticD.of(-1, +3).integralAbs(), Rational.of(5, 6));
    assertEquals(LagrangeQuadraticD.of(+1, -3).integralAbs(), Rational.of(5, 6));
    assertEquals(LagrangeQuadraticD.of(-2, +4).integralAbs(), Rational.of(6, 6));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> LagrangeQuadraticD.of(null, RealScalar.ONE));
    assertThrows(Exception.class, () -> LagrangeQuadraticD.of(RealScalar.ONE, null));
  }
}
