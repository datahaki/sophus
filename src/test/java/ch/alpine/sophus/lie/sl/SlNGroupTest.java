package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.mat.DiagonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.spa.SparseArray;
import showcase.GroupCheck;

class SlNGroupTest {
  @Test
  void testSl2() {
    SlNGroup slNGroup = new SlNGroup(2);
    // GroupCheck.showBasis(slNGroup);
    assertTrue(slNGroup.toString().contains("2"));
    Tensor matrixBasis = slNGroup.matrixBasis();
    assertEquals(matrixBasis.length(), 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
    Tensor ad = matrixAlgebra.ad();
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    ExactTensorQ.require(form);
    assertInstanceOf(SparseArray.class, form);
    assertTrue(DiagonalMatrixQ.of(form));
    Scalar scalar = Det.of(form);
    assertEquals(scalar, RealScalar.of(-8));
    int dim = matrixAlgebra.dimensions();
    Tensor trial = RandomVariate.of(DiscreteUniformDistribution.of(-30, 40), dim);
    Tensor matrix = matrixAlgebra.toMatrix(trial);
    Tolerance.CHOP.requireZero(Trace.of(matrix));
    Tensor vector = matrixAlgebra.toVector(matrix);
    ExactTensorQ.require(vector);
    assertEquals(vector, trial);
    {
      Distribution distribution = TriangularDistribution.of(-0.1, 0, 0.1);
      Tensor x = RandomVariate.of(distribution, dim);
      Tensor y = RandomVariate.of(distribution, dim);
      Tensor X = slNGroup.exponential0().exp(matrixAlgebra.toMatrix(x));
      Tensor Y = slNGroup.exponential0().exp(matrixAlgebra.toMatrix(y));
      Tensor Z = slNGroup.combine(X, Y);
      Tensor z = matrixAlgebra.toVector(slNGroup.exponential0().log(Z));
      Tolerance.CHOP.requireClose(z, BakerCampbellHausdorff.of(ad, 9).apply(x, y));
      Tolerance.CHOP.requireClose(z, BakerCampbellHausdorff.of(ad, 0xA).apply(x, y));
      Tolerance.CHOP.requireClose(z, BakerCampbellHausdorff.of(ad, 11).apply(x, y));
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4 })
  void testSl3(int n) {
    SlNGroup slNGroup = new SlNGroup(n);
    assertTrue(slNGroup.toString().contains("" + n));
    Tensor matrixBasis = slNGroup.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
    Tensor ad = matrixAlgebra.ad();
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    // IO.println(Pretty.of(form));
    ExactTensorQ.require(form);
    assertInstanceOf(SparseArray.class, form);
    Scalar scalar = Det.of(form);
    ExactScalarQ.require(scalar);
    GroupCheck.check(slNGroup);
  }
}
