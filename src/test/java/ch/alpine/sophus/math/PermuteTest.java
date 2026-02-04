package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;

class PermuteTest {
  @Test
  void test() {
    assertThrows(Exception.class, () -> Permute.of(Tensors.vector(3)));
  }
}
