// code by jph
package ch.alpine.sophus.hs.sn;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnFastMeanTest {
  @Test
  void testSnNormalized() {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.of(1, 0.2);
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, random, n, d).stream().map(Vector2Norm.NORMALIZE));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, n));
        Tensor mean1 = SnFastMean.INSTANCE.mean(sequence, weights);
        Tensor mean2 = SnManifold.INSTANCE.biinvariantMean(Chop._14).mean(sequence, weights);
        Chop._04.requireClose(mean1, mean2);
      }
  }
}
