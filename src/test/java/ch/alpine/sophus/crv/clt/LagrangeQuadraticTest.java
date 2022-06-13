// code by jph
package ch.alpine.sophus.crv.clt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.InterpolatingPolynomial;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class LagrangeQuadraticTest {
  @Test
  void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = //
        LagrangeQuadratic.interp(RealScalar.of(2), RealScalar.of(-3), RealScalar.of(7));
    Scalar p0 = lagrangeQuadratic.apply(RealScalar.ZERO);
    Scalar pm = lagrangeQuadratic.apply(RationalScalar.HALF);
    Scalar p1 = lagrangeQuadratic.apply(RealScalar.ONE);
    assertEquals(p0, RealScalar.of(2));
    assertEquals(pm, RealScalar.of(-3));
    assertEquals(p1, RealScalar.of(7));
  }

  @Test
  void testExamples() {
    LagrangeQuadratic lagrangeQuadratic = //
        LagrangeQuadratic.interp(RealScalar.of(5), RealScalar.of(7), RealScalar.of(13));
    Scalar angle = lagrangeQuadratic.apply(RealScalar.of(11));
    assertEquals(angle, RealScalar.of(973));
    Scalar p0 = lagrangeQuadratic.apply(RealScalar.ZERO);
    Scalar pm = lagrangeQuadratic.apply(RationalScalar.HALF);
    Scalar p1 = lagrangeQuadratic.apply(RealScalar.ONE);
    assertEquals(p0, RealScalar.of(5));
    assertEquals(pm, RealScalar.of(7));
    assertEquals(p1, RealScalar.of(13));
  }

  @Test
  void testNeville() {
    ScalarUnaryOperator scalarUnaryOperator = InterpolatingPolynomial.of( //
        Tensors.vector(0, 0.5, 1)).scalarUnaryOperator(Tensors.vector(5, 7, 13));
    LagrangeQuadratic clothoidQuadratic = //
        LagrangeQuadratic.interp(RealScalar.of(5), RealScalar.of(7), RealScalar.of(13));
    Distribution distribution = UniformDistribution.of(-1, 2);
    Tensor domain = RandomVariate.of(distribution, 10);
    Tolerance.CHOP.requireClose(domain.map(clothoidQuadratic), domain.map(scalarUnaryOperator));
  }
}
