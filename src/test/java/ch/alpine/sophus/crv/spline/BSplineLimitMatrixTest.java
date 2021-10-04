// code by jph
package ch.alpine.sophus.crv.spline;

import ch.alpine.sophus.math.StochasticMatrixQ;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class BSplineLimitMatrixTest extends TestCase {
  public void testSimple() {
    for (int degree = 0; degree < 5; ++degree)
      for (int n = 1; n < 10; ++n) {
        Tensor tensor = BSplineLimitMatrix.string(n, degree);
        ExactTensorQ.require(tensor);
        StochasticMatrixQ.requireRows(tensor, Chop._08);
        StochasticMatrixQ.requireRows(Inverse.of(tensor), Chop._08);
        // System.out.println("n=" + n + " degree=" + degree);
        // System.out.println(Pretty.of(tensor));
      }
  }

  public void testNonPositiveFail() {
    AssertFail.of(() -> BSplineLimitMatrix.string(0, 2));
    AssertFail.of(() -> BSplineLimitMatrix.string(-1, 2));
  }
}
