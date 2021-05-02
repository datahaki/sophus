// code by jph
package ch.alpine.sophus.math.sample;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

  public void testPermutations() {
    Tensor vector = Tensors.vector(1, 2, 3).unmodifiable();
    Set<Tensor> set = new HashSet<>();
    for (int index = 0; index < 100; ++index)
      set.add(RandomSample.of(vector));
    assertEquals(set.size(), 6);
  }
}
