// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdBiinvariantMeanTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Random random = new Random();
    BiinvariantMean biinvariantMean = Serialization.copy(SpdBiinvariantMean.INSTANCE);
    Distribution distribution = UniformDistribution.unit();
    int fails = 0;
    for (int n = 2; n < 4; ++n)
      try {
        int count = random.nextInt(4);
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, sequence.length()));
        Tensor mean = biinvariantMean.mean(sequence, weights);
        Chop._06.requireAllZero(new MeanDefect(sequence, weights, SpdManifold.INSTANCE.exponential(mean)).tangent());
      } catch (Exception exception) {
        ++fails;
        exception.printStackTrace();
      }
    assertTrue(fails <= 1);
  }

  public void testTransformSon() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n) {
      int count = random.nextInt(5);
      int fn = n;
      int len = n * n + count;
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
      Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
      Tensor g = RandomSample.of(SoRandomSample.of(n));
      Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
      Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
      Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
    }
  }

  public void testTransformGln() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    int fails = 0;
    for (int n = 2; n < 4; ++n)
      try {
        int count = random.nextInt(3);
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
        Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Tensor g = RandomVariate.of(distribution, fn, fn);
        Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
        Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
        Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
      } catch (Exception exception) {
        ++fails;
        exception.printStackTrace();
      }
    assertTrue(fails <= 1);
  }
}
