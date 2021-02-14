// code by jph
package ch.ethz.idsc.sophus.crv.dubins;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.OrderedQ;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
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
      DubinsPath dubinsPath = FixedRadiusDubins.of(start, end, RealScalar.ONE).stream().min(dubinsPathComparator).get();
      Scalar scalar = VectorNorm2.between(start.extract(0, 2), end.extract(0, 2));
      assertTrue(Scalars.lessThan(scalar, dubinsPath.length()));
      Tensor segments = dubinsPath.segments();
      OrderedQ.require(segments);
      assertEquals(segments.length(), 3);
      assertEquals(dubinsPath.length(0), segments.Get(0));
    }
  }
}
