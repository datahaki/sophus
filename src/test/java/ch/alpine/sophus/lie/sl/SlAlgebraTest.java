// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.sl2.Sl2Algebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.ad.KillingForm;
import ch.alpine.tensor.mat.re.Det;
import junit.framework.TestCase;

public class SlAlgebraTest extends TestCase {
  public void testSl2Match() {
    LieAlgebra slAlgebra = SlAlgebra.of(2);
    assertEquals(slAlgebra.basis(), Sl2Algebra.INSTANCE.basis());
    assertEquals(slAlgebra.ad(), Sl2Algebra.INSTANCE.ad());
  }

  public void testSl3Klling() {
    LieAlgebra slAlgebra = SlAlgebra.of(3);
    Tensor form = KillingForm.of(slAlgebra.ad());
    assertTrue(Scalars.nonZero(Det.of(form)));
    // System.out.println(Det.of(form));
    // System.out.println(Pretty.of(form));
    HsAlgebra hsAlgebra = new HsAlgebra(slAlgebra.ad(), 6, 6);
    assertEquals(hsAlgebra.dimH(), 2);
    AssertFail.of(() -> new HsAlgebra(slAlgebra.ad(), 5, 6));
  }
}
