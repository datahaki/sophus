// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.HsCoordinates;
import ch.alpine.sophus.dv.LeveragesCoordinate;
import ch.alpine.sophus.dv.MetricCoordinate;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;

class SpdManifoldTest {
  public static BarycentricCoordinate[] list() {
    // return GbcHelper.barycentrics(SpdManifold.INSTANCE);
    return new BarycentricCoordinate[] { //
        new HsCoordinates(new HsDesign(SpdManifold.INSTANCE), MetricCoordinate.of(InversePowerVariogram.of(1))), //
        new HsCoordinates(new HsDesign(SpdManifold.INSTANCE), MetricCoordinate.of(InversePowerVariogram.of(2))), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(new HsDesign(SpdManifold.INSTANCE), InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(new HsDesign(SpdManifold.INSTANCE), InversePowerVariogram.of(2)), //
    };
  }

  @Test
  void testSimple() {
    Random random1 = new Random();
    int d = 2;
    int fail = 0;
    int len = 5 + random1.nextInt(3);
    RandomSampleInterface rsi = new Spd0RandomSample(d, NormalDistribution.standard());
    Random random = new Random();
    Tensor sequence = RandomSample.of(rsi, random, len);
    for (BarycentricCoordinate barycentricCoordinate : list())
      try {
        Tensor point = RandomSample.of(rsi, random);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
        Tensor spd = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      } catch (Exception exception) {
        ++fail;
      }
    // System.out.println(fail);
    assertTrue(fail < 4);
  }

  @Test
  void testLagrangeProperty() {
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
      Tolerance.CHOP.requireClose(weights, UnitVector.of(len, index));
      Tensor spd = SpdManifold.INSTANCE.biinvariantMean(Chop._10).mean(sequence, weights);
      Tolerance.CHOP.requireClose(spd, point);
    }
  }

  @Test
  void testFlipMidpoint() {
    RandomSampleInterface spd = new Spd0RandomSample(3, NormalDistribution.standard());
    Tensor p = RandomSample.of(spd);
    Tensor q = RandomSample.of(spd);
    Tolerance.CHOP.requireClose(new SpdExponential(p).flip(q), SpdManifold.INSTANCE.flip(p, q));
    Tolerance.CHOP.requireClose(new SpdExponential(p).midpoint(q), SpdManifold.INSTANCE.midpoint(p, q));
  }

  @Test
  void testSplit() {
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
  void testIdentity() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m = SpdManifold.INSTANCE.split(p, p, t);
      Chop._04.requireClose(m, p);
    }
  }

  @Test
  void testNorm() {
    for (int n = 1; n < 6; ++n) {
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor g = RandomSample.of(rsi);
      Scalar dP = StaticHelper.norm(g);
      Tensor ginv = Symmetrize.of(Inverse.of(g));
      Scalar dN = StaticHelper.norm(ginv);
      Chop._06.requireClose(dP, dN);
    }
  }

  @Test
  void testSymmetryAndInvariance() {
    for (int n = 1; n < 6; ++n) {
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(rsi);
      Tensor q = RandomSample.of(rsi);
      Scalar pq = SpdManifold.INSTANCE.distance(p, q);
      Scalar qp = SpdManifold.INSTANCE.distance(q, p);
      Chop._06.requireClose(pq, qp);
      Tensor v = RandomVariate.of(NormalDistribution.standard(), n, n);
      Scalar v_pq = SpdManifold.INSTANCE.distance( //
          BasisTransform.ofForm(p, v), //
          BasisTransform.ofForm(q, v));
      Chop._06.requireClose(pq, v_pq);
      Scalar d2 = FrobeniusNorm.of(new SpdExponential(p).log(q));
      Scalar d3 = LowerVectorize0_2Norm.INSTANCE.norm(new SpdExponential(p).vectorLog(q));
      Chop._08.requireClose(d2, d3);
    }
  }

  @Test
  void testLogExp() {
    for (int n = 1; n < 4; ++n) {
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(rsi);
      Tensor q = RandomSample.of(rsi);
      Exponential exponential = new SpdExponential(p);
      Tensor log = exponential.log(q);
      Chop._06.requireClose(exponential.exp(log), q);
    }
  }

  @Test
  void testScalarProd() {
    for (int n = 1; n < 6; ++n) {
      RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(rsi);
      Tensor q = RandomSample.of(rsi);
      Exponential exponential = new SpdExponential(p);
      Tensor w1 = exponential.log(q);
      Scalar r1 = Sqrt.FUNCTION.apply(new SpdRiemann(p).scalarProd(w1, w1));
      Scalar r2 = SpdManifold.INSTANCE.distance(p, q);
      Chop._06.requireClose(r1, r2);
    }
  }

  @SuppressWarnings("unused")
  @Test
  void testDistance() {
    RandomSampleInterface randomSampleInterface = new Spd0RandomSample(3, NormalDistribution.of(0, 0.2));
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Scalar d1 = SpdManifold.INSTANCE.distance(p, q);
    Tensor v = SpdManifold.INSTANCE.exponential(p).vectorLog(q);
    Scalar d2 = SpdManifold.INSTANCE.norm(v);
    // TODO SOPHUS TEST inconsistency
    // Tolerance.CHOP.requireClose(d1, d2);
  }
}
