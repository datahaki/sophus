// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class LieMidpointTest extends TestCase {
  public void testSimple() {
    LieMidpoint lieMidpointInterface = //
        new LieMidpoint(RnGroup.INSTANCE, RnExponential.INSTANCE);
    Tensor tensor = lieMidpointInterface.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }

  public void testFailNull1() {
    try {
      new LieMidpoint(null, RnExponential.INSTANCE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailNull2() {
    try {
      new LieMidpoint(RnGroup.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
