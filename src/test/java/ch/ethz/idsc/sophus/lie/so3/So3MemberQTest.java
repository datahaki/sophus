// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3MemberQTest extends TestCase {
  private static final MemberQ MEMBER_Q = So3MemberQ.of(Chop._06);

  public void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    MEMBER_Q.requireTangent(IdentityMatrix.of(3), wedge);
    assertFalse(MEMBER_Q.isTangent(TestHelper.spawn_So3(), wedge));
  }

  public void testNullFail() {
    try {
      So3MemberQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
