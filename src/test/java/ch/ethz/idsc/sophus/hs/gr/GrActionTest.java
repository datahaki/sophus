// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
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
