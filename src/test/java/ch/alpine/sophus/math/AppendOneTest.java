// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class AppendOneTest extends TestCase {
  public void testSimple() {
    Tensor vector = Tensors.vector(2, 3);
    Tensor result = AppendOne.FUNCTION.apply(vector);
    assertEquals(vector, Tensors.vector(2, 3));
    assertEquals(result, Tensors.vector(2, 3, 1));
  }
}
