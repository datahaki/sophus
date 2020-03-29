// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class HsMidpointTest extends TestCase {
  public void testSimple() {
    HsMidpoint lieMidpoint = new HsMidpoint(RnManifold.HS_EXP);
    Tensor tensor = lieMidpoint.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }

  public void testFailNull1() {
    try {
      new HsMidpoint(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
