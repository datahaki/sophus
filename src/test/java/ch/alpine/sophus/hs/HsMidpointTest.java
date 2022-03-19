// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

public class HsMidpointTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    HsMidpoint hsMidpoint = Serialization.copy(new HsMidpoint(RnManifold.INSTANCE));
    Tensor tensor = hsMidpoint.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }

  @Test
  public void testFailNull1() {
    assertThrows(Exception.class, () -> new HsMidpoint(null));
  }
}
