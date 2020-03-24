// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.util.Random;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.MatrixQ;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class So3RandomSampleTest extends TestCase {
  private static final Random RANDOM = new Random();

  public void testSimple() {
    for (int count = 0; count < 10; ++count) {
      Tensor matrix = So3RandomSample.INSTANCE.randomSample(RANDOM);
      MatrixQ.requireSize(matrix, 3, 3);
      OrthogonalMatrixQ.require(matrix);
      Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    }
  }
}
