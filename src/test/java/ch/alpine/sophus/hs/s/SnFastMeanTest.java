// code by jph
package ch.alpine.sophus.hs.s;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnFastMeanTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5 })
  void testSnNormalized(int d) {
    RandomGenerator randomGenerator = new Random(3);
    Distribution distribution = NormalDistribution.of(1, 0.2);
    for (int n = d + 1; n < 10; ++n) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, randomGenerator, n, d).stream().map(Vector2Norm.NORMALIZE));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, randomGenerator, n));
      Tensor mean1 = SnFastMean.INSTANCE.mean(sequence, weights);
      Tensor mean2 = SnManifold.INSTANCE.biinvariantMean().mean(sequence, weights);
      Chop._04.requireClose(mean1, mean2);
    }
  }
}
