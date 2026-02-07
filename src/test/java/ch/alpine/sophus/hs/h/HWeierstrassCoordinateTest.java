// code by jph
package ch.alpine.sophus.hs.h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

class HWeierstrassCoordinateTest {
  @Test
  void test() {
    HWeierstrassCoordinate hWeierstrassCoordinate = new HWeierstrassCoordinate(Tensors.vector(0, 0));
    Tensor p = hWeierstrassCoordinate.toPoint();
    assertEquals(p, Tensors.vector(0, 0, 1));
    ExactTensorQ.require(p);
  }
}
