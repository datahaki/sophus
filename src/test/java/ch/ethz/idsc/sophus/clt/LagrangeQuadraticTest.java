// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.itp.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class LagrangeQuadraticTest extends TestCase {
  public void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = //
        LagrangeQuadratic.interp(RealScalar.of(2), RealScalar.of(-3), RealScalar.of(7));
    Scalar p0 = lagrangeQuadratic.apply(RealScalar.ZERO);
    Scalar pm = lagrangeQuadratic.apply(RationalScalar.HALF);
    Scalar p1 = lagrangeQuadratic.apply(RealScalar.ONE);
    assertEquals(p0, RealScalar.of(2));
    assertEquals(pm, RealScalar.of(-3));
    assertEquals(p1, RealScalar.of(7));
  }

  public void testExamples() {
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

  public void testNeville() {
    ScalarUnaryOperator scalarUnaryOperator = InterpolatingPolynomial.of( //
        Tensors.vector(0, 0.5, 1)).scalarUnaryOperator(Tensors.vector(5, 7, 13));
    LagrangeQuadratic clothoidQuadratic = //
        LagrangeQuadratic.interp(RealScalar.of(5), RealScalar.of(7), RealScalar.of(13));
    Distribution distribution = UniformDistribution.of(-1, 2);
    Tensor domain = RandomVariate.of(distribution, 10);
    Tolerance.CHOP.requireClose(domain.map(clothoidQuadratic), domain.map(scalarUnaryOperator));
  }
}
