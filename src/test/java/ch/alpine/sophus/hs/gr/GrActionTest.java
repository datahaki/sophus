// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrActionTest extends TestCase {
  public void testDecomp() {
    int n = 5;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor so = GrAction.match(p, q);
      Chop._10.requireClose(new GrAction(so).apply(p), q);
    }
  }
}
