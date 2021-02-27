// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import junit.framework.TestCase;

public class StRandomSampleTest extends TestCase {
  public void testSimple() {
    RandomSampleInterface randomSampleInterface = StRandomSample.of(5, 3);
    Tensor matrix = RandomSample.of(randomSampleInterface);
    OrthogonalMatrixQ.require(matrix);
    StMemberQ.INSTANCE.require(matrix);
  }

  public void testFail() {
    AssertFail.of(() -> StRandomSample.of(3, -1));
    AssertFail.of(() -> StRandomSample.of(3, 4));
    AssertFail.of(() -> StRandomSample.of(-3, -4));
  }
}
