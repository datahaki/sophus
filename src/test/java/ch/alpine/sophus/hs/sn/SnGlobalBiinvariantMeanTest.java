// code by jph
package ch.alpine.sophus.hs.sn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnGlobalBiinvariantMeanTest {
  @Test
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int dim = 2; dim < 5; ++dim)
      for (int length = 1; length < 10; ++length) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, length, dim).stream().map(Vector2Norm.NORMALIZE));
        Tensor mean = SnGlobalBiinvariantMean.INSTANCE.mean(sequence, RandomVariate.of(distribution, length));
        Chop._10.requireClose(mean, Vector2Norm.NORMALIZE.apply(mean));
      }
  }
}
