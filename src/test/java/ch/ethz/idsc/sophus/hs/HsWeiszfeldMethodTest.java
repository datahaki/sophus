// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.SpatialMedian;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsWeiszfeldMethodTest extends TestCase {
  public void testSimple() {
    SpatialMedian sm1 = HsWeiszfeldMethod.of(RnBiinvariantMean.INSTANCE, RnMetric.INSTANCE, Tolerance.CHOP);
    SpatialMedian sm2 = SpatialMedian.with(Tolerance.CHOP);
    Distribution distribution = NormalDistribution.standard();
    for (int d = 2; d < 5; ++d)
      for (int n = 2; n < 20; n += 4) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        Tensor p1 = sm1.uniform(sequence).get();
        Tensor p2 = sm2.uniform(sequence).get();
        Chop._07.requireClose(p1, p2);
      }
  }
}
