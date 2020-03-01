// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdMetricTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 6; ++n) {
      Tensor g = TestHelper.generateSpd(n);
      Scalar dP = SpdExponential.nSquared(g);
      Tensor ginv = Symmetrize.of(Inverse.of(g));
      Scalar dN = SpdExponential.nSquared(ginv);
      Chop._06.requireClose(dP, dN);
    }
  }

  public void testSwap() {
    for (int n = 1; n < 6; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      Tensor q = TestHelper.generateSpd(n);
      Scalar pq = SpdMetric.INSTANCE.distance(p, q);
      Scalar qp = SpdMetric.INSTANCE.distance(q, p);
      Chop._06.requireClose(pq, qp);
    }
  }

  public void testExpLog() {
    for (int n = 1; n < 2; ++n) {
      Tensor p = TestHelper.generateSpd(n);
      SpdExp spdExp = new SpdExp(p);
      Tensor q = TestHelper.generateSpd(n);
      Tensor log = spdExp.log(q);
      log.add(log);
      Scalar distance = SpdMetric.INSTANCE.distance(p, q);
      distance.add(distance);
    }
  }
}
