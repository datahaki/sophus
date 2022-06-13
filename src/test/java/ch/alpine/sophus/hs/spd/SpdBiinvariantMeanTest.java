// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdBiinvariantMeanTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    BiinvariantMean biinvariantMean = Serialization.copy(SpdManifold.INSTANCE.biinvariantMean(Chop._10));
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(4);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, sequence.length()));
      Tensor mean = biinvariantMean.mean(sequence, weights);
      Chop._06.requireAllZero(new MeanDefect(sequence, weights, SpdManifold.INSTANCE.exponential(mean)).tangent());
    }
  }

  @Test
  void testTransformSon() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(5);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, len));
      Tensor mL = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sequence, weights);
      Tensor g = RandomSample.of(SoRandomSample.of(n), random);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }

  @Test
  void testTransformGln() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(3);
      int len = n * n + count;
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, len));
      Tensor mL = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sequence, weights);
      Tensor g = RandomVariate.of(distribution, random, n, n);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }
}
