// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Ramp;
import junit.framework.TestCase;

public class HnHypotTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.of(0, 1000);
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      Chop._08.requireClose(HnHypot.of(x, n -> Ramp.FUNCTION.apply(n.negate())), RealScalar.ONE);
    }
  }
}
