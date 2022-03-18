// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.math.bch.BakerCampbellHausdorff;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.spa.Normal;
import ch.alpine.tensor.spa.SparseArray;

public class MatrixAlgebraTest {
  @Test
  public void testSe2() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor b2 = LeviCivitaTensor.of(3).get(2).negate();
    assertTrue(b2 instanceof SparseArray);
    Tensor basis = Tensors.of(b0, b1, b2);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    assertEquals(matrixAlgebra.ad(), Se2Algebra.INSTANCE.ad());
    assertEquals(matrixAlgebra.toVector(b1.add(b2)), Tensors.vector(0, 1, 1));
    assertEquals(matrixAlgebra.toVector(b0.subtract(b2)), Tensors.vector(1, 0, -1));
    Tensor matrix = matrixAlgebra.toMatrix(Tensors.vector(2, 3, 4));
    Tensor tensor = Total.of(Tensors.of( //
        b0.multiply(RealScalar.of(2)), //
        b1.multiply(RealScalar.of(3)), //
        b2.multiply(RealScalar.of(4))));
    assertEquals(matrix, tensor);
    Tolerance.CHOP.requireClose( //
        matrixAlgebra.toVector(N.DOUBLE.of(b1.add(b2.multiply(RealScalar.of(0.3))))), Tensors.vector(0, 1, 0.3));
    Tensor rank4 = JacobiIdentity.of(matrixAlgebra.ad());
    assertTrue(rank4 instanceof SparseArray);
  }

  @Test
  public void testSl2() {
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, 2, 2));
  }

  @Test
  public void testSo3() {
    Tensor basis = LeviCivitaTensor.of(3).negate();
    Tensor ad = new MatrixAlgebra(basis).ad();
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, -2, -2));
  }

  @Test
  public void testB0B2() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1[m]}, {0, 0, 0[m]}, {0, 0, 0}}");
    b0.set(Quantity.of(0, "m^-1"), 2, 0);
    b0.set(Quantity.of(0, "m^-1"), 2, 1);
    Tensor b2 = Normal.of(LeviCivitaTensor.of(3).get(2).negate());
    b2.set(Quantity.of(0, "m"), 0, 2);
    b2.set(Quantity.of(0, "m"), 1, 2);
    b2.set(Quantity.of(0, "m^-1"), 2, 0);
    b2.set(Quantity.of(0, "m^-1"), 2, 1);
    b2.dot(b0);
  }

  @Test
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

  @Test
  public void testUnivariate() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Tensors.of(IdentityMatrix.of(5)));
    assertEquals(matrixAlgebra.toVector(IdentityMatrix.of(5)), UnitVector.of(1, 0));
    String string = matrixAlgebra.toString();
    // System.out.println(string);
    assertTrue(string.startsWith("MatrixAlgebra["));
  }

  @Test
  public void testNumericFail() {
    AssertFail.of(() -> new MatrixAlgebra(new HeAlgebra(1).ad().map(N.DOUBLE)));
  }

  @Test
  public void testZeroFail() {
    AssertFail.of(() -> new MatrixAlgebra(Array.zeros(1, 2, 2)));
  }

  @Test
  public void testRedundantFail() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    AssertFail.of(() -> new MatrixAlgebra(Tensors.of(b0, b0)));
  }

  private static void check(MatrixAlgebra matrixAlgebra, int degree) {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor ad = matrixAlgebra.ad();
    int n = ad.length();
    BinaryOperator<Tensor> bch = BakerCampbellHausdorff.of(ad.map(N.DOUBLE), degree);
    Random random = new Random(10);
    for (int count = 0; count < 5; ++count) {
      Tensor x = RandomVariate.of(distribution, random, n);
      Tensor y = RandomVariate.of(distribution, random, n);
      Tensor z = bch.apply(x, y);
      Tensor mX = MatrixExp.of(matrixAlgebra.toMatrix(x));
      Tensor mY = MatrixExp.of(matrixAlgebra.toMatrix(y));
      Tensor mZ = MatrixLog.of(mX.dot(mY));
      Tensor z_cmp = matrixAlgebra.toVector(mZ);
      Tolerance.CHOP.requireClose(z, z_cmp);
    }
  }

  @Test
  public void testMatrixLogExpExpSe2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2Algebra.INSTANCE.basis());
    check(matrixAlgebra, 8);
  }

  @Test
  public void testMatrixLogExpExpSo3() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Algebra.INSTANCE.basis());
    check(matrixAlgebra, 8);
  }
}
