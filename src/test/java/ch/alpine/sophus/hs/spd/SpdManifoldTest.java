// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.gbc.LeveragesCoordinate;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdManifoldTest {
  public static final BarycentricCoordinate[] list() {
    // return GbcHelper.barycentrics(SpdManifold.INSTANCE);
    return new BarycentricCoordinate[] { //
        HsCoordinates.wrap(SpdManifold.INSTANCE, MetricCoordinate.of(InversePowerVariogram.of(1))), //
        HsCoordinates.wrap(SpdManifold.INSTANCE, MetricCoordinate.of(InversePowerVariogram.of(2))), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(SpdManifold.INSTANCE, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(SpdManifold.INSTANCE, InversePowerVariogram.of(2)), //
    };
  }

  @Test
  public void testSimple() {
    Random random = new Random();
    int d = 2;
    int fail = 0;
    int len = 5 + random.nextInt(3);
    RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.standard());
    Tensor sequence = RandomSample.of(rsi, len);
    for (BarycentricCoordinate barycentricCoordinate : list())
      try {
        Tensor point = RandomSample.of(rsi);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
        Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      } catch (Exception exception) {
        ++fail;
      }
    assertTrue(fail < 4);
  }

  @Test
  public void testLagrangeProperty() {
    Random random = new Random();
    int d = 2;
    int len = 5 + random.nextInt(3);
    RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.standard());
    Tensor sequence = RandomSample.of(rsi, len);
    for (BarycentricCoordinate barycentricCoordinate : list()) {
      int index = random.nextInt(sequence.length());
      Tensor point = sequence.get(index);
      Tensor weights = barycentricCoordinate.weights(sequence, point);
      AffineQ.require(weights, Chop._08);
      Chop._06.requireClose(weights, UnitVector.of(len, index));
      Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
      Chop._08.requireClose(spd, point);
    }
  }

  @Test
  public void testFlipMidpoint() {
    RandomSampleInterface spd = new Spd0RandomSample(3, NormalDistribution.standard());
    Tensor p = RandomSample.of(spd);
    Tensor q = RandomSample.of(spd);
    Tolerance.CHOP.requireClose(new SpdExponential(p).flip(q), SpdManifold.INSTANCE.flip(p, q));
    Tolerance.CHOP.requireClose(new SpdExponential(p).midpoint(q), SpdManifold.INSTANCE.midpoint(p, q));
  }

  @Test
  public void testSplit() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m1 = SpdManifold.INSTANCE.split(p, q, t);
      Tensor m2 = SpdManifold.INSTANCE.split(q, p, RealScalar.ONE.subtract(t));
      Chop._04.requireClose(m1, m2);
    }
  }

  @Test
  public void testIdentity() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m = SpdManifold.INSTANCE.split(p, p, t);
      Chop._04.requireClose(m, p);
    }
  }
}
