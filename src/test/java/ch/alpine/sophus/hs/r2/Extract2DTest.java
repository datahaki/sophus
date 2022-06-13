// code by jph
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;

class Extract2DTest {
  @Test
  void testSimple() {
    assertEquals(Extract2D.FUNCTION.apply(Tensors.vector(1, 2, 4)), Tensors.vector(1, 2));
  }

  @Test
  void testFailScalar() {
    assertThrows(Exception.class, () -> Extract2D.FUNCTION.apply(RealScalar.ONE));
  }

  @Test
  void testFailEmpty() {
    assertThrows(Exception.class, () -> Extract2D.FUNCTION.apply(Tensors.empty()));
  }

  @Test
  void testFailOne() {
    assertThrows(Exception.class, () -> Extract2D.FUNCTION.apply(Tensors.vector(1)));
  }
}
