// code by jph
package ch.alpine.sophus.flt;

import java.util.function.Function;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.win.DirichletWindow;
import junit.framework.TestCase;

public class WindowSideExtrapolationTest extends TestCase {
  public void testSimple() {
    Function<Integer, Tensor> function = WindowSideExtrapolation.of(DirichletWindow.FUNCTION);
    Tensor tensor = function.apply(4);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{-1/6, -1/6, -1/6, 3/2}"));
  }
}
