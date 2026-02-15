// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;

class SpdManifoldTest {
  @Test
  void testFlipMidpoint() {
    RandomSampleInterface spd = new Spd0RandomSample(3, NormalDistribution.of(0, 0.2));
    Tensor p = RandomSample.of(spd);
    Tensor q = RandomSample.of(spd);
    SpdExponential exp_p = new SpdExponential(p);
    Chop._10.requireClose(exp_p.exp(exp_p.log(q).negate()), SpdManifold.INSTANCE.flip(p, q));
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 4, 5 })
  void testSplitIdentity(int n) {
    RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
    {
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit(20));
      Tensor m1 = SpdManifold.INSTANCE.split(p, q, t);
      Tensor m2 = SpdManifold.INSTANCE.split(q, p, RealScalar.ONE.subtract(t));
      Chop._04.requireClose(m1, m2);
    }
    {
      Tensor p = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m = SpdManifold.INSTANCE.split(p, p, t);
      Chop._04.requireClose(m, p);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 4, 5 })
  void testNorm(int n) {
    RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
    Tensor g = RandomSample.of(rsi);
    Scalar dP = Spd0Exponential.norm(g);
    Tensor ginv = Symmetrize.of(Inverse.of(g));
    Scalar dN = Spd0Exponential.norm(ginv);
    Chop._06.requireClose(dP, dN);
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
    FrobeniusNorm.of(new SpdExponential(p).log(q));
    // Scalar d3 = LowerVectorize0_2Norm.INSTANCE.norm(new SpdExponential(p).vectorLog(q));
    // Chop._08.requireClose(d2, d3);
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
    RandomSampleInterface rsi = new Spd0RandomSample(3, NormalDistribution.of(0, 0.2));
    Tensor p = RandomSample.of(rsi);
    Tensor q = RandomSample.of(rsi);
    Scalar d1 = SpdManifold.INSTANCE.distance(p, q);
    // Tensor v = SpdManifold.INSTANCE.exponential(p).vectorLog(q);
    // Scalar d2 = SpdManifold.INSTANCE.norm(v);
    // TODO SOPHUS TEST inconsistency
    // Tolerance.CHOP.requireClose(d1, d2);
  }
}
