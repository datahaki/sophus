// code by jph
package ch.alpine.sophus.lie.su;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.PlausibleRational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Diagonal;

class Su3AlgebraTest {
  @Test
  void testSimple() {
    Su3Algebra su3Algebra = Su3Algebra.INSTANCE;
    Tensor ad = su3Algebra.ad();
    // System.out.println(ad);
    ad = ad.map(PlausibleRational.FUNCTION);
    // System.out.println(ad);
    // assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    // System.out.println(Pretty.of(form));
    Tolerance.CHOP.requireClose(Diagonal.of(form), ConstantArray.of(RealScalar.of(3), 8));
  }
}
