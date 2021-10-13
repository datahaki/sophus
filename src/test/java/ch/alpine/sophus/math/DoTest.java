// code by jph
package ch.alpine.sophus.math;

import java.util.function.Supplier;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class DoTest extends TestCase {
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
