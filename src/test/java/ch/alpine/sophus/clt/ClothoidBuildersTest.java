// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Sign;
import junit.framework.TestCase;

public class ClothoidBuildersTest extends TestCase {
  public void testCurvature() {
    Distribution distribution = UniformDistribution.unit();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Sign.requirePositive(ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder().curve(p, q).length());
    Sign.requirePositive(ClothoidBuilders.SE2_LEGENDRE.clothoidBuilder().curve(p, q).length());
  }

  public void testValuesSize() {
    assertTrue(2 <= ClothoidBuilders.values().length);
  }
}
