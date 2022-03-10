// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.ad.HigherJacobiIdentity;
import ch.alpine.sophus.lie.ad.KillingForm;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.Det;
import junit.framework.TestCase;

public class SlAlgebraTest extends TestCase {
  public void testSl2Match() {
    LieAlgebra slAlgebra = SlAlgebra.of(2);
    assertEquals(slAlgebra.basis(), Sl2Algebra.INSTANCE.basis());
    assertEquals(slAlgebra.ad(), Sl2Algebra.INSTANCE.ad());
  }

  public void testSl3Klling() {
    LieAlgebra lieAlgebra = SlAlgebra.of(3);
    Tensor ad = lieAlgebra.ad();
    Tensor form = KillingForm.of(ad);
    assertTrue(Scalars.nonZero(Det.of(form)));
    // System.out.println(Det.of(form));
    // System.out.println(Pretty.of(form));
    HsAlgebra hsAlgebra = new HsAlgebra(lieAlgebra.ad(), 6, 6);
    assertEquals(hsAlgebra.dimH(), 2);
    AssertFail.of(() -> new HsAlgebra(lieAlgebra.ad(), 5, 6));
    HigherJacobiIdentity.of4(ad);
    HigherJacobiIdentity.of4b(ad);
    HigherJacobiIdentity.of5(ad);
    HigherJacobiIdentity.of5b(ad);
  }
}
