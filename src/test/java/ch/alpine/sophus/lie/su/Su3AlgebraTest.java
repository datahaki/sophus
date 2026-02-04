// code by jph
package ch.alpine.sophus.lie.su;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.PlausibleRational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.spa.SparseArray;

class Su3AlgebraTest {
  @Test
  void testSimple() {
    Su3Algebra su3Algebra = Su3Algebra.INSTANCE;
    Tensor ad = su3Algebra.ad();
    ad = ad.map(PlausibleRational.of(10));
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    Tolerance.CHOP.requireClose(Diagonal.of(form), ConstantArray.of(RealScalar.of(3), 8));
  }
}
