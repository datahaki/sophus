// code by jph
package ch.alpine.sophus.lie.su;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.ad.KillingForm;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.spa.SparseArray;
import junit.framework.TestCase;

public class Su3AlgebraTest extends TestCase {
  public void testSimple() {
    Su3Algebra su3Algebra = Su3Algebra.INSTANCE;
    assertTrue(su3Algebra.ad() instanceof SparseArray);
    Tensor form = KillingForm.of(su3Algebra.ad());
    // System.out.println(Pretty.of(form));
    Sign.requirePositive(Det.of(form));
  }
}
