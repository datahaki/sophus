// code by jph
package ch.alpine.sophus.lie.su;

import ch.alpine.sophus.lie.KillingForm;
import ch.alpine.sophus.math.PlausibleRational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.SparseArray;
import junit.framework.TestCase;

public class Su3AlgebraTest extends TestCase {
  public void testSimple() {
    Su3Algebra su3Algebra = Su3Algebra.INSTANCE;
    Tensor ad = su3Algebra.ad();
    // System.out.println(ad);
    ad = ad.map(PlausibleRational.FUNCTION);
    // System.out.println(ad);
    assertTrue(ad instanceof SparseArray);
    Tensor form = KillingForm.of(ad);
    // System.out.println(Pretty.of(form));
    Tolerance.CHOP.requireClose(Diagonal.of(form), ConstantArray.of(RealScalar.of(3), 8));
  }

  public void testHermitian() {
    Su3Algebra.INSTANCE.basis().stream().forEach(HermitianMatrixQ::require);
    Tensor tensor = Tensor.of(Su3Algebra.INSTANCE.basis().stream().map(Trace::of));
    Chop.NONE.requireAllZero(tensor);
  }
}
