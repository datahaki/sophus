// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class DoTest {
  @Test
  public void testSimple() {
    Supplier<Tensor> supplier = new Supplier<>() {
      int count = 0;

      @Override
      public Tensor get() {
        return Tensors.vector(++count);
      }
    };
    Tensor tensor = Do.of(supplier, 3);
    assertEquals(tensor, Tensors.vector(3));
  }
}
