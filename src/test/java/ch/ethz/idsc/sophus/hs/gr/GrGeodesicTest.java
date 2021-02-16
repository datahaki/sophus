// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsMidpoint;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrGeodesicTest extends TestCase {
  public void testMidpoint() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      GrExponential exp_p = new GrExponential(p);
      GrExponential exp_q = new GrExponential(q);
      Tensor m1 = HsMidpoint.of(exp_p, q);
      Tensor m2 = exp_p.midpoint(q);
      Chop._08.requireClose(m1, m2);
      Tensor m3 = GrGeodesic.INSTANCE.midpoint(p, q);
      Chop._08.requireClose(m1, m3);
      Tensor m4 = exp_q.midpoint(p);
      Chop._08.requireClose(m1, m4);
    }
  }

  public void testMirror() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      ScalarTensorFunction stf = GrGeodesic.INSTANCE.curve(p, q);
      Tensor mir1 = stf.apply(RealScalar.ONE.negate());
      GrExponential exp_p = new GrExponential(p);
      Tensor mir2 = exp_p.flip(q);
      Chop._08.requireClose(mir1, mir2);
    }
  }
}
