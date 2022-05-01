// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

public class ControlLr2Test {
  @Test
  public void testSimple() {
    Tensor result = ControlLr2.INSTANCE.cyclic(Tensors.vector(1, 0, 0, 0));
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{1/2, 1/4, 0, 1/4}"));
  }
}
