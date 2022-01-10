// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.lie.so3.So3BiinvariantMean;
import ch.alpine.sophus.lie.so3.So3Exponential;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class BchBiinvariantMeanTest extends TestCase {
  public void testSe2Mean4() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Tensor p0 = Tensors.vector(0.1, 0.2, 0.05);
    Tensor p1 = Tensors.vector(0.02, -0.1, -0.04);
    Tensor p2 = Tensors.vector(-0.05, 0.03, 0.1);
    Tensor p3 = Tensors.vector(0.07, -0.08, -0.06);
    Tensor sequence = Tensors.of(p0, p1, p2, p3);
    Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(0.3, 0.4, 0.5, 0.6));
    Tensor meanG = Se2CoveringBiinvariantMean.INSTANCE.mean(seqG, weights);
    Tensor mean = exponential.log(meanG);
    BiinvariantMean biinvariantMean = BchBiinvariantMean.of(Se2Algebra.INSTANCE.bch(6), Chop._10);
    Tensor meanb = biinvariantMean.mean(sequence, weights);
    Chop._09.requireClose(mean, meanb);
  }

  public void testSe2MeanRandom() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Distribution dist_w = UniformDistribution.of(0.5, 1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_w, n));
      Tensor meanG = Se2CoveringBiinvariantMean.INSTANCE.mean(seqG, weights);
      Tensor mean = exponential.log(meanG);
      BiinvariantMean biinvariantMean = BchBiinvariantMean.of(Se2Algebra.INSTANCE.bch(6), Chop._10);
      Tensor meanb = biinvariantMean.mean(sequence, weights);
      Chop._09.requireClose(mean, meanb);
    }
  }

  public void testSo3Mean4() {
    Exponential exponential = So3Exponential.INSTANCE;
    Tensor p0 = Tensors.vector(0.1, 0.2, 0.05);
    Tensor p1 = Tensors.vector(0.02, -0.1, -0.04);
    Tensor p2 = Tensors.vector(-0.05, 0.03, 0.1);
    Tensor p3 = Tensors.vector(0.07, -0.08, -0.06);
    Tensor sequence = Tensors.of(p0, p1, p2, p3);
    Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(0.3, 0.4, 0.5, 0.6));
    Tensor meanG = So3BiinvariantMean.INSTANCE.mean(seqG, weights);
    Tensor mean = exponential.log(meanG);
    BiinvariantMean biinvariantMean = BchBiinvariantMean.of(So3Algebra.INSTANCE.bch(6), Chop._10);
    Tensor meanb = biinvariantMean.mean(sequence, weights);
    Chop._09.requireClose(mean, meanb);
  }

  public void testSo3MeanRandom() {
    Exponential exponential = So3Exponential.INSTANCE;
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Distribution dist_w = UniformDistribution.of(0.5, 1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_w, n));
      Tensor meanG = So3BiinvariantMean.INSTANCE.mean(seqG, weights);
      Tensor mean = exponential.log(meanG);
      BiinvariantMean biinvariantMean = BchBiinvariantMean.of(So3Algebra.INSTANCE.bch(6), Chop._10);
      Tensor meanb = biinvariantMean.mean(sequence, weights);
      Chop._09.requireClose(mean, meanb);
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> BchBiinvariantMean.of(null, Chop._10));
    AssertFail.of(() -> BchBiinvariantMean.of(So3Algebra.INSTANCE.bch(6), null));
  }
}
