// code by jph
package ch.ethz.idsc.sophus.hs.s1;

import java.util.Arrays;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class S1RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = RandomSample.of(S1RandomSample.INSTANCE, 10);
    assertEquals(Dimensions.of(tensor), Arrays.asList(10, 2));
  }
}
