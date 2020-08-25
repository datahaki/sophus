// code by jph
package ch.ethz.idsc.sophus.crv.dubins;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import junit.framework.TestCase;

public class DubinsPathComparatorTest extends TestCase {
  public void testSimple() {
    assertNotNull(DubinsPathComparator.LENGTH);
    assertNotNull(DubinsPathComparator.TOTAL_CURVATURE);
  }

  public void testValid() {
    Distribution distribution = UniformDistribution.of(-3, 3);
    Tensor start = RandomVariate.of(distribution, 3);
    Tensor end = RandomVariate.of(distribution, 3);
    for (DubinsPathComparator dubinsPathComparator : DubinsPathComparator.values()) {
      DubinsPath dubinsPath = FixedRadiusDubins.of(start, end, RealScalar.ONE).allValid().min(dubinsPathComparator).get();
      Scalar scalar = Norm._2.between(start.extract(0, 2), end.extract(0, 2));
      assertTrue(Scalars.lessThan(scalar, dubinsPath.length()));
    }
  }
}
