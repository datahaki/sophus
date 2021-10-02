// code by jph
package ch.alpine.sophus.math.d2;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class ControlLr2Test extends TestCase {
  public void testSimple() {
    Tensor result = ControlLr2.INSTANCE.cyclic(Tensors.vector(1, 0, 0, 0));
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{1/2, 1/4, 0, 1/4}"));
  }
}
