// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;

class DodgsonSabinHelperTest {
  @Test
  public void testCrafted() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2.2, 0.5);
    Tensor c = Tensors.vector(3, 1.5);
    Tensor d = Tensors.vector(3.5, 2.9);
    Tensor tensor = DodgsonSabinHelper.midpoint(a, b, c, d);
    Chop._13.requireClose(tensor, Tensors.fromString("{2.64619661516195, 0.8388990046231528}"));
  }

  @Test
  public void testCraftedReverse() {
    Tensor d = Tensors.vector(1, 1.1);
    Tensor c = Tensors.vector(2.2, 0.5);
    Tensor b = Tensors.vector(3, 1.5);
    Tensor a = Tensors.vector(3.5, 2.9);
    Tensor tensor = DodgsonSabinHelper.midpoint(a, b, c, d);
    Chop._13.requireClose(tensor, Tensors.fromString("{2.64619661516195, 0.8388990046231528}"));
  }

  @Test
  public void testLine() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2, 1.1);
    Tensor c = Tensors.vector(3, 1.1);
    Tensor d = Tensors.vector(4, 1.1);
    Tensor tensor = DodgsonSabinHelper.midpoint(a, b, c, d);
    assertEquals(tensor, Tensors.vector(2.5, 1.1));
  }

  @Test
  public void testLineDiagonal() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2, 2.1);
    Tensor c = Tensors.vector(3, 3.1);
    Tensor d = Tensors.vector(4, 4.1);
    Tensor tensor = DodgsonSabinHelper.midpoint(a, b, c, d);
    assertEquals(tensor, Tensors.vector(2.5, 2.6));
  }

  @Test
  public void testLineThree() {
    Tensor a = Tensors.vector(1, 1.1);
    Tensor b = Tensors.vector(2, 1.1);
    Tensor c = Tensors.vector(3, 1.1);
    Tensor tensor = DodgsonSabinHelper.midpoint(a, b, c);
    assertEquals(tensor, Tensors.vector(1.5, 1.1));
  }
}
