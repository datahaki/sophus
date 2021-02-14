// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.util.Arrays;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import junit.framework.TestCase;

public class TSnProjectionTest extends TestCase {
  public void testSimple() {
    Tensor x = RandomSample.of(SnRandomSample.of(2));
    Tensor matrix = TSnProjection.of(x);
    OrthogonalMatrixQ.require(matrix);
    assertEquals(Dimensions.of(matrix), Arrays.asList(2, 3));
  }
}
