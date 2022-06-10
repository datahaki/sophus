// code by jph
package ch.alpine.sophus.hs.ad;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.lie.so3.So3Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class HsBiinvariantMeanTest {
  @Test
  public void testMean() {
    Distribution distributionX = UniformDistribution.of(-0.05, 0.05);
    Distribution distributionW = UniformDistribution.of(0.2, 1);
    LieAlgebra lieAlgebra = So3Algebra.INSTANCE;
    HsAlgebra hsAlgebra = new HsAlgebra(lieAlgebra.ad(), 2, 8);
    assertFalse(hsAlgebra.isHTrivial());
    Random random = new Random(1);
    for (int n = 3; n < 7; ++n) {
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributionW, random, n));
      Tensor sequence_m = RandomVariate.of(distributionX, random, n, 2);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
      Tensor m_avg = biinvariantMean.mean(sequence_m, weights);
      SnExponential snExponential = new SnExponential(UnitVector.of(3, 2));
      Tensor pointsS2 = Tensor.of(sequence_m.stream().map(r -> r.copy().append(RealScalar.ZERO)).map(snExponential::exp));
      Tensor meanS2 = SnManifold.INSTANCE.biinvariantMean(Chop._14).mean(pointsS2, weights);
      Tensor res = snExponential.log(meanS2).extract(0, 2);
      Tolerance.CHOP.requireClose(m_avg, res);
    }
  }

  @Test
  public void testSe2Mean4() {
    Exponential exponential = Se2CoveringGroup.INSTANCE;
    Tensor p0 = Tensors.vector(0.1, 0.2, 0.05);
    Tensor p1 = Tensors.vector(0.02, -0.1, -0.04);
    Tensor p2 = Tensors.vector(-0.05, 0.03, 0.1);
    Tensor p3 = Tensors.vector(0.07, -0.08, -0.06);
    Tensor sequence = Tensors.of(p0, p1, p2, p3);
    Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(0.3, 0.4, 0.5, 0.6));
    Tensor meanG = Se2CoveringBiinvariantMean.INSTANCE.mean(seqG, weights);
    Tensor mean = exponential.log(meanG);
    Tensor ad = Se2Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
    Tensor meanb = biinvariantMean.mean(sequence, weights);
    Tolerance.CHOP.requireClose(mean, meanb);
  }

  @Test
  public void testSe2MeanRandom() {
    Exponential exponential = Se2CoveringGroup.INSTANCE;
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Distribution dist_w = UniformDistribution.of(0.5, 1);
    Tensor ad = Se2Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    Random random = new Random(1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_w, random, n));
      Tensor meanG = Se2CoveringBiinvariantMean.INSTANCE.mean(seqG, weights);
      Tensor mean = exponential.log(meanG);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
      Tensor meanb = biinvariantMean.mean(sequence, weights);
      Chop._11.requireClose(mean, meanb);
    }
  }

  @Test
  public void testSo3Mean4() {
    Exponential exponential = So3Group.INSTANCE;
    Tensor p0 = Tensors.vector(0.1, 0.2, 0.05);
    Tensor p1 = Tensors.vector(0.02, -0.1, -0.04);
    Tensor p2 = Tensors.vector(-0.05, 0.03, 0.1);
    Tensor p3 = Tensors.vector(0.07, -0.08, -0.06);
    Tensor sequence = Tensors.of(p0, p1, p2, p3);
    Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(0.3, 0.4, 0.5, 0.6));
    Tensor meanG = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(seqG, weights);
    Tensor mean = exponential.log(meanG);
    Tensor ad = So3Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
    Tensor meanb = biinvariantMean.mean(sequence, weights);
    Chop._10.requireClose(mean, meanb);
  }

  @Test
  public void testSo3MeanRandom() {
    Exponential exponential = So3Group.INSTANCE;
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Distribution dist_w = UniformDistribution.of(0.5, 1);
    Tensor ad = So3Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    Random random = new Random(1);
    for (int n = 4; n < 7; ++n) {
      Tensor sequence = RandomVariate.of(distribution, random, n, 3);
      Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(dist_w, random, n));
      Tensor meanG = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(seqG, weights);
      Tensor mean = exponential.log(meanG);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
      Tensor meanb = biinvariantMean.mean(sequence, weights);
      Chop._11.requireClose(mean, meanb);
    }
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> HsBiinvariantMean.of(null));
    assertThrows(Exception.class, () -> HsBiinvariantMean.of(null, Chop._10));
    Tensor ad = So3Algebra.INSTANCE.ad();
    HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
    assertThrows(Exception.class, () -> HsBiinvariantMean.of(hsAlgebra, null));
  }
}
