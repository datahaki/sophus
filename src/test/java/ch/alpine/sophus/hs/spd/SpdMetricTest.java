// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;
import junit.framework.TestCase;

public class SpdMetricTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 6; ++n) {
      Tensor g = TestHelper.generateSpd(n);
      Scalar dP = StaticHelper.norm(g);
      Tensor ginv = Symmetrize.of(Inverse.of(g));
      Scalar dN = StaticHelper.norm(ginv);
      Chop._06.requireClose(dP, dN);
    }
  }

  public void testSymmetryAndInvariance() {
    for (int n = 1; n < 6; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
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

  public void testLogExp() {
    for (int n = 1; n < 4; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Exponential exponential = new SpdExponential(p);
      Tensor q = TestHelper.generateSpd(n);
      Tensor log = exponential.log(q);
      Chop._06.requireClose(exponential.exp(log), q);
    }
  }

  public void testScalarProd() {
    for (int n = 1; n < 6; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Exponential exponential = new SpdExponential(p);
      Tensor q = TestHelper.generateSpd(n);
      Tensor w1 = exponential.log(q);
      Scalar r1 = Sqrt.FUNCTION.apply(new SpdRiemann(p).scalarProd(w1, w1));
      Scalar r2 = SpdMetric.INSTANCE.distance(p, q);
      Chop._06.requireClose(r1, r2);
    }
  }
}
