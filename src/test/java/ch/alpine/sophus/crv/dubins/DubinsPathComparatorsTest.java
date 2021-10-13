// code by jph
package ch.alpine.sophus.crv.dubins;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class DubinsPathComparatorsTest extends TestCase {
  public void testSimple() {
    assertNotNull(DubinsPathComparators.LENGTH);
    assertNotNull(DubinsPathComparators.TOTAL_CURVATURE);
  }

  public void testValid() {
    Distribution distribution = UniformDistribution.of(-3, 3);
    Tensor start = RandomVariate.of(distribution, 3);
    Tensor end = RandomVariate.of(distribution, 3);
    for (DubinsPathComparators dubinsPathComparator : DubinsPathComparators.values()) {
      DubinsPath dubinsPath = FixedRadiusDubins.of(start, end, RealScalar.ONE).stream().min(dubinsPathComparator).orElseThrow();
      Scalar scalar = Vector2Norm.between(start.extract(0, 2), end.extract(0, 2));
      assertTrue(Scalars.lessThan(scalar, dubinsPath.length()));
      Tensor segments = dubinsPath.segments();
      OrderedQ.require(segments);
      assertEquals(segments.length(), 3);
      assertEquals(dubinsPath.length(0), segments.Get(0));
    }
  }
}
