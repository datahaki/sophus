// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnPointNormTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.of(0, 1000);
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      Scalar scalar = HnPointNorm.INSTANCE.apply(x);
      Chop._08.requireClose(scalar, RealScalar.ONE);
    }
  }
}
