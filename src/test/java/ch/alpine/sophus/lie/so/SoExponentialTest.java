// code by jph
package ch.alpine.sophus.lie.so;

import java.util.Random;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SoExponentialTest extends TestCase {
  public void testSimple() {
    Random random = new Random(1);
    for (int n = 2; n < 10; ++n) {
      Tensor p = SoRandomSample.of(n).randomSample(random);
      Tensor q = SoRandomSample.of(n).randomSample(random);
      Exponential exponential = SoManifold.INSTANCE.exponential(p);
      Tensor vp = exponential.log(q);
      TSoMemberQ.INSTANCE.require(vp);
      Tensor qr = exponential.exp(vp);
      Chop._06.requireClose(q, qr);
      Tensor log = exponential.vectorLog(q);
      assertEquals(log.length(), n * (n - 1) / 2);
    }
  }
}
