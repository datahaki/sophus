// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsMidpoint;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrMidpointTest extends TestCase {
  public void testSimple() {
    int n = 4;
    int k = 2;
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    GrExponential exp_p = new GrExponential(p);
    Tensor m1 = HsMidpoint.of(exp_p, q);
    Tensor m2 = exp_p.midpoint(q);
    Chop._08.requireClose(m1, m2);
  }
}
