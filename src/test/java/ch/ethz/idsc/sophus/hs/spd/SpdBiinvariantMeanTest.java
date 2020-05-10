// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdBiinvariantMeanTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    BiinvariantMean biinvariantMean = Serialization.copy(SpdBiinvariantMean.INSTANCE);
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 5; ++count) {
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, sequence.length()));
        Tensor mean = biinvariantMean.mean(sequence, weights);
        MeanDefect meanDefect = BiinvariantMeanDefect.of(SpdManifold.INSTANCE);
        Chop._06.requireAllZero(meanDefect.defect(sequence, weights, mean));
      }
  }

  public void testTransformOn() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 5; ++count) {
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
        Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Tensor g = RandomSample.of(SonRandomSample.of(fn));
        Tensor sR = Tensor.of(sequence.stream().map(t -> g.dot(t).dot(Transpose.of(g))));
        Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
        Tensor mM = Transpose.of(g).dot(mR).dot(g);
        Chop._06.requireClose(mL, mM);
      }
  }

  public void testTransformGln() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 5; ++count) {
        int fn = n;
        int len = n * n + count;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), len);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, len));
        Tensor mL = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Tensor g = RandomVariate.of(distribution, fn, fn);
        Tensor sR = Tensor.of(sequence.stream().map(t -> g.dot(t).dot(Transpose.of(g))));
        Tensor mR = SpdBiinvariantMean.INSTANCE.mean(sR, weights);
        Tensor gi = Inverse.of(g);
        Tensor mM = gi.dot(mR).dot(Transpose.of(gi));
        Chop._06.requireClose(mL, mM);
      }
  }
}
