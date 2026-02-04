// code by jph
package ch.alpine.sophus.hs.s;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnGlobalBiinvariantMeanTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4 })
  void testSimple(int dim) {
    Distribution distribution = NormalDistribution.standard();
    for (int length = 1; length < 10; ++length) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, length, dim).stream().map(Vector2Norm.NORMALIZE));
      Tensor mean = SnGlobalBiinvariantMean.INSTANCE.mean(sequence, RandomVariate.of(distribution, length));
      Chop._10.requireClose(mean, Vector2Norm.NORMALIZE.apply(mean));
    }
  }
}
