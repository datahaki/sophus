// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import junit.framework.TestCase;

public class TSpdMemberQTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 10; ++n)
      TSpdMemberQ.INSTANCE.require(TestHelper.generateSim(n));
  }
}
