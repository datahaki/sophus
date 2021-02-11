// code by jph
package ch.ethz.idsc.sophus.lie.so;

import java.util.Random;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SoExponentialTest extends TestCase {
  public void testSimple() {
    Random random = new Random();
    for (int n = 2; n < 10; ++n) {
      Tensor p = SoRandomSample.of(n).randomSample(random);
      Tensor q = SoRandomSample.of(n).randomSample(random);
      Exponential sonExponential = SoManifold.INSTANCE.exponential(p);
      Tensor vp = sonExponential.log(q);
      TSoMemberQ.INSTANCE.require(vp);
      Tensor qr = sonExponential.exp(vp);
      Chop._06.requireClose(q, qr);
      Tensor log = sonExponential.vectorLog(q);
      assertEquals(log.length(), n * (n - 1) / 2);
    }
  }
}
