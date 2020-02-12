// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdSqrtTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        Tensor g = TestHelper.generateSpd(n);
        Tensor sqrt = TestHelper.sqrt(g);
        Chop._08.requireClose(sqrt.dot(sqrt), g);
        SpdSqrt spdSqrt = Serialization.copy(new SpdSqrt(g));
        Chop._10.requireClose(spdSqrt.forward(), sqrt);
        Chop._10.requireClose(Inverse.of(spdSqrt.forward()), spdSqrt.inverse());
      }
  }
}
