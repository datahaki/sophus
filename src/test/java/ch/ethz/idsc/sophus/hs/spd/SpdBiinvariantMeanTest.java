// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdBiinvariantMeanTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    BiinvariantMean biinvariantMean = Serialization.copy(SpdBiinvariantMean.INSTANCE);
    Distribution distribution = UniformDistribution.unit();
    int fails = 0;
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 4; ++count)
        try {
          int fn = n;
          int len = n * n + count;
          Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
          Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, sequence.length()));
          Tensor mean = biinvariantMean.mean(sequence, weights);
          Chop._06.requireAllZero(new MeanDefect(sequence, weights, SpdManifold.INSTANCE.exponential(mean)).tangent());
        } catch (Exception e) {
          ++fails;
        }
    assertTrue(fails < 3);
  }

  public void testTransformSon() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 5; ++count) {
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
        Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Tensor g = RandomSample.of(SonRandomSample.of(n));
        Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
        Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
        Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
      }
  }

  public void testTransformGln() {
    Distribution distribution = UniformDistribution.unit();
    int fails = 0;
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 3; ++count)
        try {
          int fn = n;
          int len = n * n + count;
          Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
          Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
          Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Tensor g = RandomVariate.of(distribution, fn, fn);
          Tensor sR = Tensor.of(sequence.stream().map(t -> BasisTransform.ofForm(t, g)));
          Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
          Chop._06.requireClose(mR, BasisTransform.ofForm(mL, g));
        } catch (Exception e) {
          System.err.println("fail");
          ++fails;
        }
    assertTrue(fails < 3);
  }
}
