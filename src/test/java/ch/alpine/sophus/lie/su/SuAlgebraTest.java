// code by jph
package ch.alpine.sophus.lie.su;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.ad.KillingForm;
import ch.alpine.sophus.lie.ad.HigherJacobiIdentity;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntihermitianMatrixQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Trace;
import junit.framework.TestCase;

public class SuAlgebraTest extends TestCase {
  public void testSimple() {
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = SuAlgebra.of(n);
      lieAlgebra.basis().forEach(AntihermitianMatrixQ::require);
      for (Tensor matrix : lieAlgebra.basis())
        Tolerance.CHOP.requireZero(Trace.of(matrix));
      Tensor form = KillingForm.of(lieAlgebra.ad());
      // System.out.println(Pretty.of(form));
      SquareMatrixQ.require(form);
      HigherJacobiIdentity.of(lieAlgebra.ad());
    }
  }
}
