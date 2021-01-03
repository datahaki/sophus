// code by jph
package ch.ethz.idsc.sophus.lie.son;

import java.util.Random;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SonExponentialTest extends TestCase {
  public void testSimple() {
    Random random = new Random();
    for (int n = 2; n < 10; ++n) {
      Tensor p = SonRandomSample.of(n).randomSample(random);
      Tensor q = SonRandomSample.of(n).randomSample(random);
      SonExponential sonExponential = new SonExponential(p);
      Tensor vp = sonExponential.log(q);
      SonMemberQ.of(Chop._08).requireTangent(p, vp);
      Tensor qr = sonExponential.exp(vp);
      Chop._06.requireClose(q, qr);
      Tensor log = sonExponential.vectorLog(q);
      assertEquals(log.length(), n * (n - 1) / 2);
    }
  }
}
