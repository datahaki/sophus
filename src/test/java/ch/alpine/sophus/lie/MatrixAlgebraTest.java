// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.MatrixBracket;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.ad.KillingForm;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.spa.Normal;
import ch.alpine.tensor.spa.SparseArray;
import junit.framework.TestCase;

public class MatrixAlgebraTest extends TestCase {
  public void testSe2() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor b2 = LeviCivitaTensor.of(3).get(2).negate();
    assertTrue(b2 instanceof SparseArray);
    Tensor basis = Tensors.of(b0, b1, b2);
    Tensor ad = new MatrixAlgebra(basis).ad();
    // System.out.println(ad);
    assertEquals(ad, LieAlgebras.se2());
  }

  public void testSl2() {
    Tensor ad = new MatrixAlgebra(LieAlgebras.sl2_basis()).ad();
    assertEquals(ad, LieAlgebras.sl2());
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(8, -8, 8));
  }

  public void testSo3() {
    Tensor basis = LeviCivitaTensor.of(3).negate();
    Tensor ad = new MatrixAlgebra(basis).ad();
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, -2, -2));
  }

  public void testB0B2() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1[m]}, {0, 0, 0[m]}, {0, 0, 0}}");
    b0.set(Quantity.of(0, "m^-1"), 2, 0);
    b0.set(Quantity.of(0, "m^-1"), 2, 1);
    Tensor b2 = Normal.of(LeviCivitaTensor.of(3).get(2).negate());
    b2.set(Quantity.of(0, "m"), 0, 2);
    b2.set(Quantity.of(0, "m"), 1, 2);
    b2.set(Quantity.of(0, "m^-1"), 2, 0);
    b2.set(Quantity.of(0, "m^-1"), 2, 1);
    // System.out.println(Pretty.of(b0));
    // System.out.println(Pretty.of(b2));
    b2.dot(b0);
  }

  public void testSe2Units() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1[m]}, {0, 0, 0[m]}, {0[m^-1], 0[m^-1], 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0[m]}, {0, 0, 1[m]}, {0[m^-1], 0[m^-1], 0}}");
    Tensor b2 = Normal.of(LeviCivitaTensor.of(3).get(2).negate());
    b2.set(Quantity.of(0, "m"), 0, 2);
    b2.set(Quantity.of(0, "m"), 1, 2);
    b2.set(Quantity.of(0, "m^-1"), 2, 0);
    b2.set(Quantity.of(0, "m^-1"), 2, 1);
    Tensor basis = Tensors.of(b0, b1, b2);
    MatrixBracket.of(b0, b1);
    MatrixBracket.of(b0, b2);
    MatrixBracket.of(b1, b2);
    Tensor ad = new MatrixAlgebra(basis).ad(); // ad is unitless
    BinaryOperator<Tensor> binaryOperator = BakerCampbellHausdorff.of(ad, 6);
    Tensor x = Tensors.fromString("{1, 2, 3}");
    ad.dot(x);
    Tensor y = Tensors.fromString("{0.3, -0.4, 0.5}");
    Tensor tensor = binaryOperator.apply(x, y);
    Tensor expect = Tensors.fromString("{2.46585069444446, 2.417638888888879, 3.5}");
    Tolerance.CHOP.requireClose(tensor, expect);
    // Tensor approx = BchApprox.of(ad).apply(x, y);
    // Tolerance.CHOP.requireClose(approx, BakerCampbellHausdorff.of(ad, BchApprox.DEGREE).apply(x, y));
  }
}
