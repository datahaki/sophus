// code by jph
package ch.alpine.sophus.lie.ad;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class JacobiIdentityTest extends TestCase {
  public void testHeisenberg() {
    Tensor ad = TestHelper.he1();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(ad, eye.get(0), eye.get(1)), eye.get(2));
    assertEquals(Dot.of(ad, eye.get(1), eye.get(0)), eye.get(2).negate());
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }

  public void testSo3() {
    Tensor so3 = TestHelper.so3();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(so3, eye.get(0), eye.get(1)), eye.get(2));
    assertEquals(Dot.of(so3, eye.get(1), eye.get(0)), eye.get(2).negate());
    assertEquals(JacobiIdentity.of(so3), Array.zeros(3, 3, 3, 3));
  }

  public void testSl2() {
    Tensor ad = TestHelper.sl2();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    ad.set(Scalar::zero, Tensor.ALL, 1, 2);
    AssertFail.of(() -> JacobiIdentity.require(ad));
  }

  public void testSe3() {
    Tensor ad = JacobiIdentity.require(TestHelper.se2());
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }
}
