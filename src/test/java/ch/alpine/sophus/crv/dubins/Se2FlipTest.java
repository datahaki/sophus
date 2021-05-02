// code by jph
package ch.alpine.sophus.crv.dubins;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class Se2FlipTest extends TestCase {
  public void testSimple() {
    Tensor tensor = Se2Flip.FUNCTION.apply(Tensors.vector(1, 2, 3));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(1, -2, -3));
  }
}
