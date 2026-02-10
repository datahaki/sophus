// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdNManifoldTest {
  @Test
  void test() {
    SpdNManifold spdNManifold = new SpdNManifold(3);
    assertEquals(spdNManifold.dimensions(), 6);
  }

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    RandomGenerator randomGenerator = new Random(3);
    BiinvariantMean biinvariantMean = Serialization.copy(SpdManifold.INSTANCE.biinvariantMean());
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = randomGenerator.nextInt(4);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, randomGenerator, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, randomGenerator, sequence.length()));
      Tensor mean = biinvariantMean.mean(sequence, weights);
      Chop._06.requireAllZero(MeanDefect.of(sequence, weights, SpdManifold.INSTANCE.exponential(mean)).tangent());
    }
  }

  @Test
  void testTransformSon() {
    RandomGenerator randomGenerator = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = randomGenerator.nextInt(5);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, randomGenerator, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, randomGenerator, len));
      Tensor mL = SpdManifold.INSTANCE.biinvariantMean().mean(sequence, weights);
      Tensor g = RandomSample.of(new SoNGroup(n), randomGenerator);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdManifold.INSTANCE.biinvariantMean().mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }

  @Test
  void testTransformGln() {
    RandomGenerator randomGenerator = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = randomGenerator.nextInt(3);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, randomGenerator, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, randomGenerator, len));
      Tensor mL = SpdManifold.INSTANCE.biinvariantMean().mean(sequence, weights);
      Tensor g = RandomVariate.of(distribution, randomGenerator, n, n);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdManifold.INSTANCE.biinvariantMean().mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }
}
