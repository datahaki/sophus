// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.KillingForm;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import junit.framework.TestCase;

public class AdBuilderTest extends TestCase {
  public void testSimple() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor b2 = LeviCivitaTensor.of(3).get(2).negate();
    Tensor basis = Tensors.of(b0, b1, b2);
    AdBuilder.of(basis);
  }

  public void testSl2() {
    Tensor ad = AdBuilder.of(LieAlgebras.sl2_basis());
    assertEquals(ad, LieAlgebras.sl2());
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(8, -8, 8));
  }

  public void testSo3() {
    Tensor basis = LeviCivitaTensor.of(3).negate();
    Tensor ad = AdBuilder.of(basis);
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, -2, -2));
  }
}
