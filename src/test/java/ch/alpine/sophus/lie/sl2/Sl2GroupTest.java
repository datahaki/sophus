// code by jph
package ch.alpine.sophus.lie.sl2;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class Sl2GroupTest extends TestCase {
  public void testSimple() {
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(Tensors.vector(8, 64, 4));
    Tensor inverse = sl2GroupElement.inverse().toCoordinate();
    ExactTensorQ.require(inverse);
    assertEquals(inverse, Tensors.vector(-2, -16, 0.25));
  }
}
