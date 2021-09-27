// code by jph
package ch.alpine.sophus.math.sample;

import java.util.Arrays;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import junit.framework.TestCase;

public class RandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor tensor = RandomSample.of(BallRandomSample.of(Tensors.vector(1, 2, 3), RealScalar.ONE), 6);
    assertEquals(Dimensions.of(tensor), Arrays.asList(6, 3));
  }
}
