// code by jph
package ch.alpine.sophus.crv.spline;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.StochasticMatrixQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.sca.Chop;

class BSplineLimitMatrixTest {
  @Test
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

  @Test
  public void testNonPositiveFail() {
    assertThrows(Exception.class, () -> BSplineLimitMatrix.string(0, 2));
    assertThrows(Exception.class, () -> BSplineLimitMatrix.string(-1, 2));
  }
}
