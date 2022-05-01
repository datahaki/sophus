// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

class Sl2GroupTest {
  @Test
  public void testSimple() {
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(Tensors.vector(8, 64, 4));
    Tensor inverse = sl2GroupElement.inverse().toCoordinate();
    ExactTensorQ.require(inverse);
    assertEquals(inverse, Tensors.vector(-2, -16, 0.25));
  }
}
