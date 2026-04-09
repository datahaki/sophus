// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.DiagonalMatrixQ;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.spa.SparseArray;

class SoNGroupTest {
  @Test
  void testString2() {
    SoNGroup soNGroup = new SoNGroup(2);
    assertTrue(soNGroup.toString().contains("2"));
  }

  @Test
  void testString3Rodrigues() {
    SoNGroup soNGroup = new SoNGroup(3);
    assertTrue(soNGroup.toString().contains("3"));
  }

  @Test
  void testBasis3() {
    SoNGroup soNGroup = new SoNGroup(3);
    assertTrue(soNGroup.toString().contains("3"));
    Tensor matrixBasis = MatrixAlgebra.of(soNGroup).basis();
    assertEquals(matrixBasis.length(), 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
    Tensor ad = matrixAlgebra.ad();
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    assertInstanceOf(SparseArray.class, form);
    assertTrue(DiagonalMatrixQ.of(form));
    Scalar scalar = Det.of(form);
    assertEquals(scalar, RealScalar.of(-8));
    int dim = matrixAlgebra.dimensions();
    Tensor trial = RandomVariate.of(DiscreteUniformDistribution.of(-30, 40), dim);
    Tensor matrix = matrixAlgebra.toMatrix(trial);
    AntisymmetricMatrixQ.INSTANCE.require(matrix);
    Tensor vector = matrixAlgebra.toVector(matrix);
    ExactTensorQ.require(vector);
    assertEquals(vector, trial);
  }

  @ParameterizedTest
  @ValueSource(ints = { 4, 5 })
  void testBasis(int n) {
    SoNGroup soNGroup = new SoNGroup(n);
    assertTrue(soNGroup.toString().contains("" + n));
  }

  @RepeatedTest(10)
  void testRandomDet(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    SoNGroup soNGroup = new SoNGroup(n);
    for (Tensor matrix : RandomSample.of(soNGroup.randomSampleInterface(), 10)) {
      OrthogonalMatrixQ.INSTANCE.require(matrix);
      Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7 })
  void testSimple(int n) {
    RandomSampleInterface randomSampleInterface = new SoNGroup(n).randomSampleInterface();
    Tensor tensor = RandomSample.of(randomSampleInterface);
    Tolerance.CHOP.requireClose(Det.of(tensor), RealScalar.ONE);
    OrthogonalMatrixQ.INSTANCE.require(tensor);
    MatrixQ.requireSize(tensor, n, n);
    Tensor log = MatrixLog.of(tensor);
    AntisymmetricMatrixQ.INSTANCE.require(log);
  }

  @Test
  void testZeroFail() {
    assertThrows(Exception.class, () -> new SoNGroup(0));
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new SoNGroup(0));
  }
}
