// code by jph
package ch.alpine.sophus.lie.sp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.spa.SparseArray;

class SymplecticFormTest {
  @Test
  void test() {
    Tensor omega = SymplecticForm.omega(2);
    assertInstanceOf(SparseArray.class, omega);
    // IO.println(Pretty.of(omega));
    assertEquals(Dimensions.of(omega), List.of(4, 4));
  }
}
