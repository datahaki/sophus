// code by jph
package ch.alpine.sophus.hs.gr;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

public class GrActionTest {
  @Test
  public void testDecomp() {
    int n = 5;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor so = GrAction.match(p, q);
      Chop._10.requireClose(new GrAction(so).apply(p), q);
    }
  }
}
