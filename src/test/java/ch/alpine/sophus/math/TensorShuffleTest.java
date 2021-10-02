// code by jph
package ch.alpine.sophus.math;

import java.util.HashSet;
import java.util.Set;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class TensorShuffleTest extends TestCase {
  public void testPermutations() {
    Tensor vector = Tensors.vector(1, 2, 3).unmodifiable();
    Set<Tensor> set = new HashSet<>();
    for (int index = 0; index < 100; ++index)
      set.add(TensorShuffle.of(vector));
    assertEquals(set.size(), 6);
  }
}
