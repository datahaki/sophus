// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class So2RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor r1 = RandomSample.of(So2RandomSample.INSTANCE);
    Tensor r2 = RandomSample.of(SoRandomSample.of(2));
    assertEquals(Dimensions.of(r1), Dimensions.of(r2));
  }
}
