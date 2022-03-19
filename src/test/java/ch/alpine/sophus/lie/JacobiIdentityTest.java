// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.IdentityMatrix;

public class JacobiIdentityTest {
  @Test
  public void testHeisenberg() {
    Tensor ad = new HeAlgebra(1).ad();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(ad, eye.get(0), eye.get(2)), eye.get(1));
    assertEquals(Dot.of(ad, eye.get(2), eye.get(0)), eye.get(1).negate());
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }

  @Test
  public void testSo3() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Tensor eye = IdentityMatrix.of(3);
    assertEquals(Dot.of(so3, eye.get(0), eye.get(1)), eye.get(2));
    assertEquals(Dot.of(so3, eye.get(1), eye.get(0)), eye.get(2).negate());
    assertEquals(JacobiIdentity.of(so3), Array.zeros(3, 3, 3, 3));
  }

  @Test
  public void testSl2() {
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    ad.set(Scalar::zero, Tensor.ALL, 1, 2);
    assertThrows(Exception.class, () -> JacobiIdentity.require(ad));
  }

  @Test
  public void testSe2() {
    Tensor ad = Se2Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
  }

  @Test
  public void testSe3() {
    Tensor ad = Se3Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), ConstantArray.of(RealScalar.ZERO, 6, 6, 6, 6));
  }
}
