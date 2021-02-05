// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.so3.So3TestHelper;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class SonMemberQTest extends TestCase {
  public void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    new TSonMemberQ(IdentityMatrix.of(3)).require(wedge);
    assertFalse(new TSonMemberQ(So3TestHelper.spawn_So3()).isMember(wedge));
  }

  public void testDet1() {
    Tensor nondet = DiagonalMatrix.of(1, 1, -1);
    assertEquals(Det.of(nondet), RealScalar.ONE.negate());
    assertFalse(SonMemberQ.INSTANCE.isMember(nondet));
  }

  public void testNullFail() {
    AssertFail.of(() -> new TSonMemberQ(null));
  }
}
