// code by jph
package ch.alpine.sophus.hs.sn;

import java.util.Arrays;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class S1RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = RandomSample.of(S1RandomSample.INSTANCE, 10);
    assertEquals(Dimensions.of(tensor), Arrays.asList(10, 2));
  }
}
