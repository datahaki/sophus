package ch.alpine.sophus.lie.sp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

class SymplecticTest {
  @Test
  void test() {
    Tensor omega = Symplectic.omega(2);
    assertEquals(Dimensions.of(omega), List.of(4, 4));
  }
}
