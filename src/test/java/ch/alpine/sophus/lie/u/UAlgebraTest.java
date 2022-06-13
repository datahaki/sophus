// code by jph
package ch.alpine.sophus.lie.u;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.KillingForm;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.mat.AntihermitianMatrixQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class UAlgebraTest {
  @Test
  void testSimple() {
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = UAlgebra.of(n);
      lieAlgebra.basis().forEach(AntihermitianMatrixQ::require);
      Tensor form = KillingForm.of(lieAlgebra.ad());
      // System.out.println(Pretty.of(form));
      SquareMatrixQ.require(form);
    }
  }

  @Test
  void testEigensystem() {
    LieAlgebra lieAlgebra = UAlgebra.of(3);
    Tensor basis = lieAlgebra.basis();
    assertEquals(basis.length(), 9);
    Tensor weights = RandomVariate.of(UniformDistribution.unit(), 9);
    Tensor matrix = weights.dot(basis);
    AntihermitianMatrixQ.require(matrix);
    Tensor divide = matrix.divide(ComplexScalar.I);
    HermitianMatrixQ.require(divide);
    Eigensystem eigensystem = Eigensystem.ofHermitian(divide);
    Tensor diag = DiagonalMatrix.with(eigensystem.values().multiply(ComplexScalar.I));
    Tensor vec = eigensystem.vectors();
    Tolerance.CHOP.requireClose(matrix, BasisTransform.ofMatrix(diag, vec));
  }
}
