// code by jph
package ch.alpine.sophus.hs.st;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;

class StMemberQTest {
  @Test
  void testSimple() {
    for (int n = 3; n < 6; ++n)
      for (int k = n - 2; k <= n; ++k) {
        RandomSampleInterface randomSampleInterface = new StRandomSample(n, k);
        Tensor matrix = RandomSample.of(randomSampleInterface);
        StMemberQ.INSTANCE.require(matrix);
      }
  }
}
