// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Ramp;
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
