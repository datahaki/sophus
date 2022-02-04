// code by ob, jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class So2GeodesicTest extends TestCase {
  public void testSimple() {
    Tensor p = So2Exponential.INSTANCE.exp(RealScalar.ONE);
    Tensor q = So2Exponential.INSTANCE.exp(RealScalar.of(2));
    Scalar split = So2Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    assertEquals(split, RationalScalar.of(3, 2));
    ExactScalarQ.require(split);
  }

  public void testEndPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int index = 0; index < 10; ++index) {
      Tensor p = So2Exponential.INSTANCE.exp(RandomVariate.of(distribution));
      Tensor q = So2Exponential.INSTANCE.exp(RandomVariate.of(distribution));
      Chop._12.requireClose(p, So2Geodesic.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._12.requireClose(q, So2Geodesic.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }
}
