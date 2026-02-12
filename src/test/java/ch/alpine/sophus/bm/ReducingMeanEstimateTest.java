// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.spd.Spd0RandomSample;
import ch.alpine.sophus.hs.spd.SpdManifold;
import ch.alpine.sophus.hs.spd.SpdPhongMean;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class ReducingMeanEstimateTest {
  private static Tensor _check(Tensor sequence, Tensor weights) {
    AffineQ.INSTANCE.require(weights);
    ReducingMeanEstimate biinvariantMean = new ReducingMeanEstimate(RGroup.INSTANCE);
    Tensor mean1 = biinvariantMean.estimate(sequence, weights);
    Tensor mean2 = LinearBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(mean1, mean2);
    return mean1;
  }

  @Test
  void testExact() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(2, 3, 0, 8, 0, 7, 1));
    Tensor sequence = Tensors.vector(0, 1, 2, 3, 4, 5, 6);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  void testRandom() {
    int n = 6;
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    _check(sequence, weights);
  }

  @Test
  void testExact2() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(1, -1, 1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
    assertEquals(mean, RealScalar.of(9));
  }

  @Test
  void testExact3() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(-1, 1, 1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  void testExact4() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, -1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  void testExactFail() {
    Tensor weights = Tensors.vector(0.0, 0.0, 0.1);
    Tensor sequence = Tensors.vector(3, 4, 10);
    ReducingMeanEstimate biinvariantMean = new ReducingMeanEstimate(RGroup.INSTANCE);
    assertThrows(Exception.class, () -> biinvariantMean.estimate(sequence, weights));
  }

  @Test
  void testSimple() {
    RandomGenerator randomGenerator = new Random(1);
    ReducingMeanEstimate bm = new ReducingMeanEstimate(SpdManifold.INSTANCE);
    for (int d = 2; d < 4; ++d) {
      int n = d * (d + 1) / 2 + 1 + randomGenerator.nextInt(3);
      RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.of(0, 0.3));
      Tensor sequence = RandomSample.of(rsi, randomGenerator, n);
      Distribution distribution = UniformDistribution.of(0.1, 1);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, randomGenerator, n));
      Tensor m0 = sequence.get(ArgMax.of(weights));
      Tensor m1 = SpdPhongMean.INSTANCE.mean(sequence, weights);
      Tensor m2 = bm.estimate(sequence, weights);
      BiinvariantMean biinvariantMean = IterativeBiinvariantMean.argmax(SpdManifold.INSTANCE, Chop._10);
      Tensor mE0 = biinvariantMean.mean(sequence, weights);
      Scalar d0 = SpdManifold.INSTANCE.distance(m0, mE0);
      Scalar d1 = SpdManifold.INSTANCE.distance(m1, mE0);
      Scalar d2 = SpdManifold.INSTANCE.distance(m2, mE0);
      assertTrue(Scalars.lessThan(d1, d0));
      Scalars.lessThan(d2, d1);
    }
  }

  @Test
  void testLagrangeProperty() {
    int d = 2;
    int len = 5 + ThreadLocalRandom.current().nextInt(3);
    RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.standard());
    Tensor sequence = RandomSample.of(rsi, len);
    BiinvariantMean biinvariantMean = SpdManifold.INSTANCE.biinvariantMean();
    for (int index = 0; index < len; ++index) {
      Tensor point = sequence.get(index);
      Tensor weights = UnitVector.of(len, index);
      AffineQ.INSTANCE.require(weights);
      Tensor spd = biinvariantMean.mean(sequence, weights);
      Chop._08.requireClose(spd, point);
    }
  }
}
