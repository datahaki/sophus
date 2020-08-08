// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsMidpoint;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnMidpointTest extends TestCase {
  public void testSimple() {
    HsMidpoint hsMidpoint = new HsMidpoint(SnManifold.INSTANCE);
    for (int dimension = 2; dimension <= 5; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor m1 = SnMidpoint.INSTANCE.midpoint(p, q);
      Tensor m2 = hsMidpoint.midpoint(p, q);
      Chop._08.requireClose(m1, m2);
    }
  }
}
