// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;

class SpdMetricTest {
  @Test
  void testSimple() {
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
      Scalar pq = SpdMetric.INSTANCE.distance(p, q);
      Scalar qp = SpdMetric.INSTANCE.distance(q, p);
      Chop._06.requireClose(pq, qp);
      Tensor v = RandomVariate.of(NormalDistribution.standard(), n, n);
      Scalar v_pq = SpdMetric.INSTANCE.distance( //
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
      Scalar r2 = SpdMetric.INSTANCE.distance(p, q);
      Chop._06.requireClose(r1, r2);
    }
  }
}
