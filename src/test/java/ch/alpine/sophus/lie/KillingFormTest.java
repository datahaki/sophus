// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.re.Det;

public class KillingFormTest {
  @Test
  public void testSe2() {
    Tensor ad = Se2Algebra.INSTANCE.ad().unmodifiable();
    JacobiIdentity.require(ad);
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    assertEquals(ad.dot(UnitVector.of(3, 0)).dot(UnitVector.of(3, 1)), Array.zeros(3));
    assertEquals(ad.dot(UnitVector.of(3, 0)).dot(UnitVector.of(3, 2)), UnitVector.of(3, 1).negate());
    assertEquals(ad.dot(UnitVector.of(3, 1)).dot(UnitVector.of(3, 2)), UnitVector.of(3, 0));
    assertEquals(KillingForm.of(ad), DiagonalMatrix.of(0, 0, -2));
  }

  @Test
  public void testSo3() {
    Tensor ad = So3Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    Tensor kil = KillingForm.of(ad);
    assertEquals(kil, DiagonalMatrix.of(-2, -2, -2));
  }

  @Test
  public void testSl2() {
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    Tensor kil = KillingForm.of(ad);
    // killing form is non-degenerate
    assertTrue(Scalars.nonZero(Det.of(kil)));
  }

  @Test
  public void testHe3() {
    Tensor ad = new HeAlgebra(1).ad();
    assertEquals(JacobiIdentity.of(ad), Array.zeros(3, 3, 3, 3));
    Tensor kil = KillingForm.of(ad);
    assertTrue(Scalars.isZero(Det.of(kil)));
  }

  @Test
  public void testRank4Fail() {
    assertThrows(Exception.class, () -> KillingForm.of(Array.zeros(3, 3, 3, 3)));
  }
}
