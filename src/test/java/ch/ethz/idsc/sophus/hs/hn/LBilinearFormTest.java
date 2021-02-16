// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.TrapezoidalDistribution;
import junit.framework.TestCase;

public class LBilinearFormTest extends TestCase {
  public void testSimple() {
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    for (int count = 0; count < 5; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      assertTrue(Scalars.lessThan(LBilinearForm.between(p, q), RealScalar.of(-1)));
    }
  }
}
