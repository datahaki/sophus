// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.spd.Spd0RandomSample;
import ch.alpine.sophus.hs.spd.SpdGeodesic;
import ch.alpine.sophus.hs.spd.SpdManifold;
import ch.alpine.sophus.hs.spd.SpdMetric;
import ch.alpine.sophus.hs.spd.SpdPhongMean;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class ReducingMeanTest {
  private static Tensor _check(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    BiinvariantMean biinvariantMean = ReducingMean.of(RnGeodesic.INSTANCE);
    Tensor mean = biinvariantMean.mean(sequence, weights);
    Tolerance.CHOP.requireClose(mean, weights.dot(sequence));
    return mean;
  }

  @Test
  public void testExact() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(2, 3, 0, 8, 0, 7, 1));
    Tensor sequence = Tensors.vector(0, 1, 2, 3, 4, 5, 6);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  public void testRandom() {
    int n = 6;
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    _check(sequence, weights);
  }

  @Test
  public void testExact2() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(1, -1, 1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
    assertEquals(mean, RealScalar.of(9));
  }

  @Test
  public void testExact3() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(-1, 1, 1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  public void testExact4() {
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, -1));
    Tensor sequence = Tensors.vector(3, 4, 10);
    Tensor mean = _check(sequence, weights);
    ExactScalarQ.require((Scalar) mean);
  }

  @Test
  public void testExactFail() {
    Tensor weights = Tensors.vector(0.0, 0.0, 0.1);
    Tensor sequence = Tensors.vector(3, 4, 10);
    BiinvariantMean biinvariantMean = ReducingMean.of(RnGeodesic.INSTANCE);
    assertThrows(Exception.class, () -> biinvariantMean.mean(sequence, weights));
  }

  @Test
  public void testSimple() {
    Random random = new Random(1);
    BiinvariantMean bm = ReducingMean.of(SpdGeodesic.INSTANCE);
    for (int d = 2; d < 4; ++d) {
      int n = d * (d + 1) / 2 + 1 + random.nextInt(3);
      RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.of(0, 0.3));
      Tensor sequence = RandomSample.of(rsi, random, n);
      Distribution distribution = UniformDistribution.of(0.1, 1);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, random, n));
      Tensor m0 = sequence.get(ArgMax.of(weights));
      Tensor m1 = SpdPhongMean.INSTANCE.mean(sequence, weights);
      Tensor m2 = bm.mean(sequence, weights);
      BiinvariantMean biinvariantMean = IterativeBiinvariantMean.of(SpdManifold.INSTANCE, Chop._10);
      Tensor mE0 = biinvariantMean.mean(sequence, weights);
      Scalar d0 = SpdMetric.INSTANCE.distance(m0, mE0);
      Scalar d1 = SpdMetric.INSTANCE.distance(m1, mE0);
      Scalar d2 = SpdMetric.INSTANCE.distance(m2, mE0);
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
}
