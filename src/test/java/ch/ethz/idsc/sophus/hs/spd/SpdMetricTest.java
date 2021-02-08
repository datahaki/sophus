// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sqrt;
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
