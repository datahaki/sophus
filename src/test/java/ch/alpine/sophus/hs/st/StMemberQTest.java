// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import junit.framework.TestCase;

public class StMemberQTest extends TestCase {
  public void testSimple() {
    for (int n = 3; n < 6; ++n)
      for (int k = n - 2; k <= n; ++k) {
        RandomSampleInterface randomSampleInterface = StRandomSample.of(n, k);
        Tensor matrix = RandomSample.of(randomSampleInterface);
        StMemberQ.INSTANCE.require(matrix);
      }
  }
}