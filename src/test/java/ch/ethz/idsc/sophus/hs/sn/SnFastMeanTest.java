// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnFastMeanTest extends TestCase {
  public void testSnNormalized() {
    Distribution distribution = NormalDistribution.of(1, 0.2);
    int fails = 0;
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n)
        try {
          Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(Vector2Norm.NORMALIZE));
          Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
          Tensor mean1 = SnFastMean.INSTANCE.mean(sequence, weights);
          Tensor mean2 = SnBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._04.requireClose(mean1, mean2);
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 2);
  }
}
