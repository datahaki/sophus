// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import junit.framework.TestCase;

public class So3RandomSampleTest extends TestCase {
  public void testSimple() {
    for (int count = 0; count < 10; ++count) {
      Tensor matrix = RandomSample.of(So3RandomSample.INSTANCE);
      MatrixQ.requireSize(matrix, 3, 3);
      OrthogonalMatrixQ.require(matrix);
      Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    }
  }
}
