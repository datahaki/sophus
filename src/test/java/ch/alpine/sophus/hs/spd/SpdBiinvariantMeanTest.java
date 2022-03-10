// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.IOException;
import java.util.Random;

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
import junit.framework.TestCase;

public class SpdBiinvariantMeanTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    BiinvariantMean biinvariantMean = Serialization.copy(SpdBiinvariantMean.INSTANCE);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(4);
      int fn = n;
      int len = n * n + count;
      RandomSampleInterface rsi = new SpdRandomSample(fn, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, sequence.length()));
      Tensor mean = biinvariantMean.mean(sequence, weights);
      Chop._06.requireAllZero(new MeanDefect(sequence, weights, SpdManifold.INSTANCE.exponential(mean)).tangent());
    }
  }

  public void testTransformSon() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(5);
      int fn = n;
      int len = n * n + count;
      RandomSampleInterface rsi = new SpdRandomSample(fn, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
      Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor g = RandomSample.of(SoRandomSample.of(n));
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }

  public void testTransformGln() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(3);
      int fn = n;
      int len = n * n + count;
      RandomSampleInterface rsi = new SpdRandomSample(fn, TriangularDistribution.with(0, 1));
      Tensor sequence = RandomSample.of(rsi, random, len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, len));
      Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor g = RandomVariate.of(distribution, random, fn, fn);
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }
}
