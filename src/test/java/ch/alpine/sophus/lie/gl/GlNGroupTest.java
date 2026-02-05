package ch.alpine.sophus.lie.gl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.spa.SparseArray;
import showcase.GroupCheck;

class GlNGroupTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3 })
  void testBasis(int n) {
    GlNGroup glNGroup = new GlNGroup(n);
    assertTrue(glNGroup.toString().contains("" + n));
    Tensor matrixBasis = glNGroup.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
    Tensor ad = matrixAlgebra.ad();
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    assertInstanceOf(SparseArray.class, form);
    Scalar scalar = Det.of(form);
    ExactScalarQ.require(scalar);
    assertEquals(scalar, RealScalar.ZERO);
    GroupCheck.check(glNGroup);
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3 })
  void testAdjoint(int n) {
    GlNGroup lieGroup = new GlNGroup(n);
    Distribution distribution = NormalDistribution.of(0, 0.3);
    Tensor g = RandomVariate.of(distribution, n, n).add(IdentityMatrix.of(n));
    Tensor X = RandomVariate.of(distribution, n, n);
    Tensor adgX = lieGroup.adjoint(g, X);
    Tensor lhs = MatrixExp.of(adgX);
    Tensor rhs = lieGroup.conjugation(g).apply(lieGroup.exponential0().exp(X));
    Tolerance.CHOP.requireClose(lhs, rhs);
  }
}
