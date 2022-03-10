// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class JacobiIdentityTest extends TestCase {
  public void testHeisenberg() {
    Tensor ad = new HeAlgebra(1).ad();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(ad, eye.get(0), eye.get(2)), eye.get(1));
    assertEquals(Dot.of(ad, eye.get(2), eye.get(0)), eye.get(1).negate());
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }

  public void testSo3() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(so3, eye.get(0), eye.get(1)), eye.get(2));
    assertEquals(Dot.of(so3, eye.get(1), eye.get(0)), eye.get(2).negate());
    assertEquals(JacobiIdentity.of(so3), Array.zeros(3, 3, 3, 3));
  }

  public void testSl2() {
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    ad.set(Scalar::zero, Tensor.ALL, 1, 2);
    AssertFail.of(() -> JacobiIdentity.require(ad));
  }

  public void testSe3() {
    Tensor ad = JacobiIdentity.require(Se3Algebra.INSTANCE.ad());
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }
}
