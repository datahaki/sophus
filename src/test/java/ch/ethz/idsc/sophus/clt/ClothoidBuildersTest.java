// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class ClothoidBuildersTest extends TestCase {
  public void testCurvature() {
    Distribution distribution = UniformDistribution.unit();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Sign.requirePositive(ClothoidBuilders.SE2_ANALYTIC.curve(p, q).length());
    Sign.requirePositive(ClothoidBuilders.SE2_LEGENDRE.curve(p, q).length());
  }

  public void testValuesSize() {
    assertEquals(ClothoidBuilders.VALUES.size(), 3);
  }
}
