// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.util.Arrays;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class TSnProjectionTest extends TestCase {
  public void testSimple() {
    for (int d = 1; d < 6; ++d) {
      Tensor x = RandomSample.of(SnRandomSample.of(d));
      Tensor matrix = TSnProjection.of(x);
      OrthogonalMatrixQ.require(matrix);
      assertEquals(Dimensions.of(matrix), Arrays.asList(d, d + 1));
      Tolerance.CHOP.requireAllZero(matrix.dot(x));
    }
  }
}
