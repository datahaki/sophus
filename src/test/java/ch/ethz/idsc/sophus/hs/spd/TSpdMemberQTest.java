// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class TSpdMemberQTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 10; ++n) {
      Tensor sim = TestHelper.generateSim(n);
      TSpdMemberQ.INSTANCE.require(sim);
      Tolerance.CHOP.requireClose(TSpdMemberQ.project(sim), sim);
    }
  }
}
