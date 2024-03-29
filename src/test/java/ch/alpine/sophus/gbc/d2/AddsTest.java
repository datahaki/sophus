// code by jph
package ch.alpine.sophus.gbc.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.RotateLeft;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

class AddsTest {
  @Test
  void testSimple() {
    assertEquals(Adds.forward(Tensors.vector(1, 2, 3)), Tensors.vector(3, 5, 4));
    assertEquals(Adds.reverse(Tensors.vector(1, 2, 3)), Tensors.vector(4, 3, 5));
  }

  @Test
  void testRandom() {
    Distribution distribution = DiscreteUniformDistribution.of(-10, 10);
    for (int n = 1; n < 10; ++n) {
      Tensor tensor = RandomVariate.of(distribution, n);
      assertEquals(RotateLeft.of(Adds.forward(tensor), -1), Adds.reverse(tensor));
    }
  }

  @Test
  void testMatrix() {
    assertEquals(Adds.matrix(3), Tensors.fromString("{{1, 1, 0}, {0, 1, 1}, {1, 0, 1}}"));
  }

  @Test
  void testNegFail() {
    assertThrows(Exception.class, () -> Adds.matrix(-1));
  }
}
