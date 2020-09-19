// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.sophus.lie.so3.So3TestHelper;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SonMemberQTest extends TestCase {
  private static final MemberQ MEMBER_Q = SonMemberQ.of(Chop._06);

  public void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    MEMBER_Q.requireTangent(IdentityMatrix.of(3), wedge);
    assertFalse(MEMBER_Q.isTangent(So3TestHelper.spawn_So3(), wedge));
  }

  public void testDet1() {
    Tensor nondet = DiagonalMatrix.of(1, 1, -1);
    assertEquals(Det.of(nondet), RealScalar.ONE.negate());
    assertFalse(MEMBER_Q.isPoint(nondet));
  }

  public void testNullFail() {
    try {
      SonMemberQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
