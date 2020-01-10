// code by jph
package ch.ethz.idsc.sophus.flt;

import java.util.function.Function;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.win.DirichletWindow;
import junit.framework.TestCase;

public class WindowSideExtrapolationTest extends TestCase {
  public void testSimple() {
    Function<Integer, Tensor> function = WindowSideExtrapolation.of(DirichletWindow.FUNCTION);
    Tensor tensor = function.apply(4);
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.fromString("{-1/6, -1/6, -1/6, 3/2}"));
  }
}
