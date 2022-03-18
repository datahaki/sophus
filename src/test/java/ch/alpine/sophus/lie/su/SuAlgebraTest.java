// code by jph
package ch.alpine.sophus.lie.su;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.HigherJacobiIdentity;
import ch.alpine.sophus.lie.KillingForm;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntihermitianMatrixQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Trace;

public class SuAlgebraTest {
  @Test
  public void testSimple() {
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = SuAlgebra.of(n);
      lieAlgebra.basis().forEach(AntihermitianMatrixQ::require);
      for (Tensor matrix : lieAlgebra.basis())
        Tolerance.CHOP.requireZero(Trace.of(matrix));
      Tensor ad = lieAlgebra.ad();
      Tensor form = KillingForm.of(ad);
      // System.out.println(Pretty.of(form));
      SquareMatrixQ.require(form);
      HigherJacobiIdentity.of4(ad);
      HigherJacobiIdentity.of4b(ad);
      HigherJacobiIdentity.of5(ad);
      HigherJacobiIdentity.of5b(ad);
    }
  }
}
