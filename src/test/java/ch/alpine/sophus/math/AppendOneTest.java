// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class AppendOneTest {
  @Test
  public void testSimple() {
    Tensor vector = Tensors.vector(2, 3);
    Tensor result = AppendOne.FUNCTION.apply(vector);
    assertEquals(vector, Tensors.vector(2, 3));
    assertEquals(result, Tensors.vector(2, 3, 1));
  }
}
