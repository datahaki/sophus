// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import junit.framework.TestCase;

public class StMemberQTest extends TestCase {
  public void testSimple() {
    RandomSampleInterface randomSampleInterface = StRandomSample.of(5, 3);
    Tensor matrix = RandomSample.of(randomSampleInterface);
    OrthogonalMatrixQ.require(matrix);
    StMemberQ.INSTANCE.require(matrix);
  }
}
