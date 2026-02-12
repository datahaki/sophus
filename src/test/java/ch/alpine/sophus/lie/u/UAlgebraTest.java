// code by jph
package ch.alpine.sophus.lie.u;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.AntihermitianMatrixQ;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class UAlgebraTest {
  @Test
  void testSimple() {
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = UAlgebra.of(n);
      UAlgebra.basis(n).forEach(AntihermitianMatrixQ.INSTANCE::require);
      Tensor form = KillingForm.of(lieAlgebra.ad());
      SquareMatrixQ.INSTANCE.require(form);
    }
  }

  @Test
  void testEigensystem() {
    Tensor basis = UAlgebra.basis(3);
    assertEquals(basis.length(), 9);
    Tensor weights = RandomVariate.of(UniformDistribution.unit(), 9);
    Tensor matrix = weights.dot(basis);
    AntihermitianMatrixQ.INSTANCE.require(matrix);
    Tensor divide = matrix.divide(ComplexScalar.I);
    HermitianMatrixQ.INSTANCE.require(divide);
    Eigensystem.ofHermitian(divide);
  }
}
