package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;

class HeFormatTest {
  @Test
  void test() {
    assertThrows(Exception.class, () -> HeFormat.of(Tensors.vector(1, 2, 3, 4)));
  }
}
