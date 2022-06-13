// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;

class TSnProjectionTest {
  @Test
  void testSimple() {
    for (int d = 1; d < 6; ++d) {
      Tensor x = RandomSample.of(SnRandomSample.of(d));
      Tensor matrix = TSnProjection.of(x);
      OrthogonalMatrixQ.require(matrix);
      assertEquals(Dimensions.of(matrix), Arrays.asList(d, d + 1));
      Tolerance.CHOP.requireAllZero(matrix.dot(x));
    }
  }
}
