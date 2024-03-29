// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.SecureRandom;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.spd.Spd0RandomSample;
import ch.alpine.sophus.hs.spd.SpdManifold;
import ch.alpine.sophus.hs.spd.SpdPhongMean;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
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
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class ReducingMeanTest {
  private static Tensor _check(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    BiinvariantMean biinvariantMean = ReducingMean.of(RnGroup.INSTANCE);
    Tensor mean = biinvariantMean.mean(sequence, weights);
    Tolerance.CHOP.requireClose(mean, weights.dot(sequence));
    return mean;
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
    BiinvariantMean biinvariantMean = ReducingMean.of(RnGroup.INSTANCE);
    assertThrows(Exception.class, () -> biinvariantMean.mean(sequence, weights));
  }

  @Test
  void testSimple() {
    RandomGenerator random = new Random(1);
    BiinvariantMean bm = ReducingMean.of(SpdManifold.INSTANCE);
    for (int d = 2; d < 4; ++d) {
      int n = d * (d + 1) / 2 + 1 + random.nextInt(3);
      RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.of(0, 0.3));
      Tensor sequence = RandomSample.of(rsi, random, n);
      Distribution distribution = UniformDistribution.of(0.1, 1);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, n));
      Tensor m0 = sequence.get(ArgMax.of(weights));
      Tensor m1 = SpdPhongMean.INSTANCE.mean(sequence, weights);
      Tensor m2 = bm.mean(sequence, weights);
      BiinvariantMean biinvariantMean = IterativeBiinvariantMean.argmax(SpdManifold.INSTANCE, Chop._10);
      Tensor mE0 = biinvariantMean.mean(sequence, weights);
      Scalar d0 = SpdManifold.INSTANCE.distance(m0, mE0);
      Scalar d1 = SpdManifold.INSTANCE.distance(m1, mE0);
      Scalar d2 = SpdManifold.INSTANCE.distance(m2, mE0);
      assertTrue(Scalars.lessThan(d1, d0));
      // assertTrue(
      Scalars.lessThan(d2, d1);
      // );
      // System.out.println("---");
      // System.out.println(d0);
      // System.out.println(d1);
      // System.out.println(d2);
    }
  }

  @Test
  void testLagrangeProperty() {
    RandomGenerator random = new SecureRandom();
    int d = 2;
    int len = 5 + random.nextInt(3);
    RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.standard());
    Tensor sequence = RandomSample.of(rsi, len);
    BiinvariantMean biinvariantMean = SpdManifold.INSTANCE.biinvariantMean(Chop._10);
    for (int index = 0; index < len; ++index) {
      Tensor point = sequence.get(index);
      Tensor weights = UnitVector.of(len, index);
      AffineQ.require(weights, Chop._08);
      Tensor spd = biinvariantMean.mean(sequence, weights);
      Chop._08.requireClose(spd, point);
    }
  }
}
