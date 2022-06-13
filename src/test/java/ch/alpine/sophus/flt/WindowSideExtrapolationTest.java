// code by jph
package ch.alpine.sophus.flt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.sca.win.DirichletWindow;

class WindowSideExtrapolationTest {
  @Test
  void testSimple() {
    Function<Integer, Tensor> function = WindowSideExtrapolation.of(DirichletWindow.FUNCTION);
    Tensor tensor = function.apply(4);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{-1/6, -1/6, -1/6, 3/2}"));
  }
}
