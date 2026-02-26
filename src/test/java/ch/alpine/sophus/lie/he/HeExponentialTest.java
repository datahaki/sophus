// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class HeExponentialTest {
  @Test
  void test() {
    Tensor matrix = HeExponential.INSTANCE.gl_representation(Tensors.vector(1, 2, 3, 4, 5));
    Tensor string = Tensors.fromString("{{0, 1, 2, 5}, {0, 0, 0, 3}, {0, 0, 0, 4}, {0, 0, 0, 0}}");
    assertEquals(matrix, string);
  }
}
