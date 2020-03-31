// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import junit.framework.TestCase;

public class HnBiinvariantMeanTest extends TestCase {
  public void testNullFail() {
    try {
      new HnBiinvariantMean(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
