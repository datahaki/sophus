// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.spa.Normal;

class LowerVectorizeTest {
  @Test
  void testEmpty() {
    assertThrows(Exception.class, () -> LowerVectorize.of(Tensors.vector(), +0));
    assertThrows(Exception.class, () -> LowerVectorize.of(Tensors.vector(), -1));
  }

  @Test
  void testMatrixN1() {
    Tensor matrix = Tensors.fromString("{{1,2,3}, {4,5,6}, {7,8,9}}");
    assertEquals(LowerVectorize.of(matrix, 1), Tensors.fromString("{1,2, 4,5,6, 7,8,9}"));
    assertEquals(LowerVectorize.of(matrix, 0), Tensors.fromString("{1, 4,5, 7,8,9}"));
    assertEquals(LowerVectorize.of(matrix, -1), Tensors.fromString("{4, 7,8 }"));
  }

  @Test
  void testScalarFail() {
    assertThrows(Exception.class, () -> LowerVectorize.of(Pi.VALUE, 0));
    assertThrows(Exception.class, () -> LowerVectorize.of(Pi.VALUE, -1));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> LowerVectorize.of(Tensors.vector(1, 2, 3), 0));
    assertThrows(Exception.class, () -> LowerVectorize.of(Tensors.vector(1, 2, 3), -1));
  }

  @Test
  void testRank3() {
    // assertThrows(Exception.class, () -> LowerVectorize.of(LeviCivitaTensor.of(3), 0));
    Tensor tensor = LowerVectorize.of(LeviCivitaTensor.of(3), 0);
    Normal.of(tensor);
  }
}
