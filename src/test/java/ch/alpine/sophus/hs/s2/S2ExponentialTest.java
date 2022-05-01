// code by jph
package ch.alpine.sophus.hs.s2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.IdentityMatrix;

class S2ExponentialTest {
  @Test
  public void testSimple() {
    S2Exponential s2Exponential = new S2Exponential(Tensors.vector(1, 0, 0));
    Tensor tensor = s2Exponential.projection();
    ExactTensorQ.require(tensor);
    assertEquals(tensor, IdentityMatrix.of(3).extract(1, 3));
  }
}
