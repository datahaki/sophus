// code by jph
package ch.alpine.sophus.crv.dub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

class Se2FlipTest {
  @Test
  void testSimple() {
    Tensor tensor = Se2Flip.FUNCTION.apply(Tensors.vector(1, 2, 3));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(1, -2, -3));
  }
}
