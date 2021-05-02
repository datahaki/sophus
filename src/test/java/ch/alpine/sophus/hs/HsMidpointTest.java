// code by jph
package ch.alpine.sophus.hs;

import java.io.IOException;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class HsMidpointTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    HsMidpoint hsMidpoint = Serialization.copy(new HsMidpoint(RnManifold.INSTANCE));
    Tensor tensor = hsMidpoint.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }

  public void testFailNull1() {
    AssertFail.of(() -> new HsMidpoint(null));
  }
}
