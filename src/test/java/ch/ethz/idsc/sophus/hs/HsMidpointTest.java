// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class HsMidpointTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    HsMidpoint hsMidpoint = Serialization.copy(new HsMidpoint(RnManifold.HS_EXP));
    Tensor tensor = hsMidpoint.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }

  public void testFailNull1() {
    AssertFail.of(() -> new HsMidpoint(null));
  }
}
