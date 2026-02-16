// code by jph
package ch.alpine.sophus.hs.st;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class StManifoldTest {
  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void test(int n) {
    for (int k = n - 2; k < n; ++k) {
      StiefelManifold stiefelManifold = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(stiefelManifold);
      Tensor q = StManifold.projection(p);
      Tolerance.CHOP.requireClose(p, q);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6, 10 })
  void testMean(int n) {
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    for (int k = 1; k <= n; ++k) {
      StiefelManifold stiefelManifold = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(stiefelManifold, randomGenerator);
      stiefelManifold.isPointQ().require(p);
      TStMemberQ tStMemberQ = new TStMemberQ(p);
      Tensor sequence = Tensors.empty();
      // IO.println(stiefelManifold.dimensions());
      int K = 8;
      Exponential exponential = stiefelManifold.exponential(p);
      for (int j = 0; j < K; ++j) {
        Tensor v = tStMemberQ.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.02), randomGenerator, k, n));
        sequence.append(exponential.exp(v));
      }
      BiinvariantMean biinvariantMean = stiefelManifold.biinvariantMean();
      Tensor weights = NormalizeTotal.FUNCTION.apply( //
          RandomVariate.of(UniformDistribution.unit(), randomGenerator, K));
      Tensor mean = biinvariantMean.mean(sequence, weights);
      {
        SoNGroup Gk = new SoNGroup(k);
        SoNGroup Gn = new SoNGroup(n);
        StAction stAction = new StAction(RandomSample.of(Gk), RandomSample.of(Gn));
        Tensor mean2 = biinvariantMean.mean(stAction.slash(sequence), weights);
        Chop._08.requireClose(mean2, stAction.apply(mean));
      }
    }
  }
}
